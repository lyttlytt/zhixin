package com.library.base;


import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by 袁从斌-where on 2016/12/29.
 * basePresenter基类
 */

public abstract class BasePresenter<V extends BaseView> {
    protected V mView;
    protected CompositeDisposable mCompositeDisposable;

    public BasePresenter(V view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    public abstract void start();

    /**
     * 释放对view引用,和取消可能正在进行的网络请求
     * release network request
     * release view reference
     */
    protected void release() {
        mCompositeDisposable.clear();
        mView = null;
    }
}
