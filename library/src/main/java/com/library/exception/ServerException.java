package com.library.exception;

/**
 * 网络请求返回的异常信息
 */
public class ServerException extends Throwable {

    public ServerException(String message) throws Exception {
        throw new Exception(message);
    }
}
