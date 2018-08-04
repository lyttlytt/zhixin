package com.shuzhengit.zhixin.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import com.library.util.LogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/17 08:58
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/crash/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;
    private CrashHandler() {
    }
    public static CrashHandler getInstance(){
        return sInstance;
    }
    public void init(Context context){
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();

    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //导出异常信息到sd卡
            dumpException2SDCard(ex);
            //上传异常信息要服务器
            uploadException2Server();
        }catch (IOException e){
            e.printStackTrace();
        }
        ex.printStackTrace();
        //如果系统提供了默认的异常处理器,则交给系统去结束程序,否则就由自己结束自己
        if (mDefaultCrashHandler!=null){
            mDefaultCrashHandler.uncaughtException(thread,ex);
        }else {
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpException2SDCard(Throwable ex) throws IOException{
        //如果sd卡不存在或无法使用,无法把异常信息写入到SD卡
        if(!Environment.getExternalStorageState().endsWith(Environment.MEDIA_MOUNTED)){
            LogUtils.e(TAG,"sd卡无法使用或不存在,跳过这次异常信息的收集");
        }
        File dir = new File(PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            printWriter.println(time);
            dumpPhoneInfo(printWriter);
            printWriter.println();
            ex.printStackTrace(printWriter);
            printWriter.close();
        }catch (Exception e){
            LogUtils.e(TAG,"存储crash信息失败");
        }
    }

    private void dumpPhoneInfo(PrintWriter printWriter) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        //app版本
        printWriter.print("app version : ");
        printWriter.print(packageInfo.versionName);
        printWriter.print("_");
        printWriter.print(packageInfo.versionCode);

        //Android版本号
        printWriter.print("  OS Version : ");
        printWriter.print(Build.VERSION.RELEASE);
        printWriter.print("_");
        printWriter.println(Build.VERSION.SDK_INT);

        //手机制造商
        printWriter.print("Vendor : ");
        printWriter.println(Build.MANUFACTURER);

        //手机型号
        printWriter.print("Model : ");
        printWriter.println(Build.MODEL);

        //CPU架构
        printWriter.print("CPU ABI : ");
        String[] supportedAbis = Build.SUPPORTED_ABIS;
        for (String supportedAbi : supportedAbis) {
            printWriter.print(supportedAbi + " ");
        }
    }

    private void uploadException2Server() {

    }

}
