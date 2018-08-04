package com.library.download;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/11 12:36
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:上传文件的监听
 */

public interface UploadListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
