package com.library.download;


import com.library.bean.FileLoadingBean;
import com.library.rx.RxBus2;
import com.library.rx.RxSchedulersHelper;
import com.library.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author：SEELE on 2016/7/25 14:48
 * Contact: studylifetime@sina.com
 */
public abstract class FileCallback implements Callback<ResponseBody> {

    //订阅下载进度
    private CompositeDisposable rxDisposable = new CompositeDisposable();

    // 目标文件存储的文件夹路径
    private String destFileDir;
    //目标文件存储的文件名
    private String destFileName;

    public FileCallback(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        subscribeLoadProgress();// 订阅下载进度
    }

    /**
     * 成功后回调
     */
     public abstract void onSuccess(File file);

    /**
     * 下载过程回调
     */
    public abstract void onLoading(long progress, long total);

    /**
     * 请求成功后保存文件
     */
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody>
            response) {
        try {
            saveFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过IO流写入文件
     */
    private  File saveFile(Response<ResponseBody> response) throws Exception {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048];
        int len;
        try {
            File dir = new File(destFileDir);
            if (!dir.exists()) {// 如果文件不存在新建一个
                dir.mkdirs();
            }
            in = response.body().byteStream();
            File file = new File(dir, destFileName);
            out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }

            // 回调成功的接口
            onSuccess(file);

            unSubscribe();// 取消订阅
            return file;
        } finally {
            IOUtils.close(in);
            IOUtils.close(out);
        }
    }

    /**
     * 订阅文件下载进度
     */
    private void subscribeLoadProgress() {
        RxBus2.getDefault().toFlowable(FileLoadingBean.class)
                .compose(RxSchedulersHelper.io_main())
                .onBackpressureBuffer()
                .subscribe(new Consumer<FileLoadingBean>() {
                    @Override
                    public void accept(FileLoadingBean fileLoadingBean) throws Exception {
                        onLoading(fileLoadingBean.getCurrentlength(),
                                fileLoadingBean.getTotal());
                    }
                });
//        rxSubscriptions.add(RxBus.getDefault()
//                .toObservable(FileLoadingBean.class)//
//                // FileLoadingBean保存了progress和total的实体类
//                .onBackpressureBuffer()
//                .compose(RxSchedulersHelper.<FileLoadingBean>io_main())
//                .subscribe(new Action1<FileLoadingBean>() {
//                    @Override
//                    public void call(FileLoadingBean fileLoadEvent) {
//                        onLoading(fileLoadEvent.getCurrentlength(),
//                                fileLoadEvent.getTotal());
//                    }
//                }));
    }

    /**
     * 取消订阅，防止内存泄漏
     */
    private void unSubscribe() {
        if (!rxDisposable.isDisposed()) {
            rxDisposable.dispose();
        }
    }
}
