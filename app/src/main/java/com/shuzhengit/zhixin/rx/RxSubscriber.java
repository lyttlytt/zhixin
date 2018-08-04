package com.shuzhengit.zhixin.rx;

import com.library.base.BaseView;
import com.library.exception.ServerException;
import com.library.util.LogUtils;
import com.library.util.NetUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;

import io.reactivex.subscribers.DisposableSubscriber;


/**
 * Author:袁从斌 on 2017/6/9 16:10
 * e-mail:whereycb@163.com
 */

public abstract class RxSubscriber<T> extends DisposableSubscriber<T> {
    private static final String TAG = "RxSubscriber";
    private BaseView mView;

    protected abstract void _onNext(T t);

    protected void _onError(String message) {
    }

    protected void _onCompleted() {
    }

    /**
     * 需要显示加载动画走这个
     *
     * @param view contract对应的view
     */
    public RxSubscriber(BaseView view) {
        mView = view;
        if (mView != null) {
            LogUtils.i(TAG, "constructor");
        }
    }

    /**
     * 默认空构造方法不做加载动画
     */
    public RxSubscriber() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mView != null) {
            LogUtils.i(TAG, "onStart");
        }
        if (mView != null) {
            mView.showLoading();
        }

    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
//            e.printStackTrace();
        if (!NetUtils.isNetworkAvailable(APP.getInstance())) {
            _onError(APP.getInstance().getString(R.string.networkNotAvailable));
            return;
        }
        if (e instanceof ServerException) {
            e.printStackTrace();
            LogUtils.d(TAG,"ServerException : "+e.getLocalizedMessage());
            _onError(e.getMessage());
        } else {
            //返回的是返回成功后还有异常
            LogUtils.d(TAG,e.getMessage());
            _onError(e.getMessage());
        }
        if (mView != null) {
            mView.hideLoading();
            mView.failure(e.getMessage());
        }
        onComplete();
        //取消订阅
        unDisposeMethod();
    }

    @Override
    public void onComplete() {
        if (mView != null) {
            mView.hideLoading();
        }
        _onCompleted();
        unDisposeMethod();
    }


    private void unDisposeMethod() {
        if (!isDisposed()) {
            dispose();
        }
    }


}
