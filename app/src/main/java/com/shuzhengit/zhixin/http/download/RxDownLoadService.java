package com.shuzhengit.zhixin.http.download;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.library.download.FileCallback;
import com.library.download.FileResponseBody;
import com.library.util.LogUtils;
import com.library.util.Retrofit2ConverterFactory;
import com.library.util.ToastUtils;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.http.ApiService;
import com.shuzhengit.zhixin.http.HttpProtocol;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/10 09:58
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class RxDownLoadService extends Service {
    private static final String TAG = "RxDownLoadService";
    //目标文件存储的文件夹路径
    private String mDestFileDir;
    //目标文件存储的文件名
    private String mDestFileName = "update.apk";
    private Context mContext;
    private int preProgress = 0;
    private int notify_id = 1000;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManagerCompat;
    private String downloadUrl;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
         this.downloadUrl = intent.getStringExtra("downloadUrl");
        loadFile(this.downloadUrl);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 下载文件
     */
    private void loadFile(String downloadUrl) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mDestFileDir = mContext.getExternalFilesDir("files") + File.separator + "download";
        } else {
            ToastUtils.showShortToast(this, "sd卡异常");
        }
        initNotification();
        ApiService apiService = initHttp();
        apiService.loadFile(downloadUrl)
                .enqueue(new FileCallback(mDestFileDir,mDestFileName) {
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        t.printStackTrace();
                        LogUtils.e(TAG, "请求失败");
                        //取消通知
                        cancelNotification();
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.e(TAG, "请求成功");
                        //取消通知栏
                        cancelNotification();
                        //安装软件
                        installApk(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        LogUtils.e(TAG, progress + "--------" + total);
                        //更新前台通知
                        updateNotification(progress * 100 / total);
                    }
                });
//                .enqueue(new FileCallback(mDestFileDir, mDestFileName) {
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//
//                    }
//
//                    @Override
//                    public void onLoading(long progress, long total) {
//
//                    }
//                });
    }

    /**
     * 更新通知
     * @param progress
     */
    private void updateNotification(long progress) {
        int currProgress = (int)progress;
        if (preProgress<currProgress){
            mBuilder.setContentText(progress+"%");
            mBuilder.setProgress(100,(int)progress,false);
            mNotificationManagerCompat.notify(notify_id,mBuilder.build());
        }
        preProgress = (int)progress;
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName()+".fileProvider",file);
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(contentUri,"application/vnd.android.package-archive");
        }else {
            Uri uri = Uri.fromFile(file);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        //执行意图进行安装
        mContext.startActivity(install);
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        mNotificationManagerCompat.cancel(notify_id);
    }

    private ApiService initHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new FileResponseBody(originalResponse))//将自定义的responseBody设置给它
                        .build();
            }
        })
                .retryOnConnectionFailure(true)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit build = new Retrofit.Builder().client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new Retrofit2ConverterFactory())
                .baseUrl(HttpProtocol.URL)
                .build();
        ApiService apiService = build.create(ApiService.class);
        return apiService;
    }

    /**
     * 初始化Notification通知
     */
    private void initNotification() {
        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.logo)
                .setContentText("0%")//进度Text
                .setContentTitle("自动更新")//标题
                .setProgress(100, 0, false);//设置进度条
        //获取系统通知管理器
        mNotificationManagerCompat = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManagerCompat.notify(notify_id, mBuilder.build());//发送通知
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
