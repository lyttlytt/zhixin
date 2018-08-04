package com.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/10 22:28
 * E-mail:yuancongbin@gmail.com
 * 和viewpager结合的fragment  取消预加载
 */
public abstract class BaseLazyLoadFragment<P extends BasePresenter> extends BaseFragment<P> {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    protected boolean isCreateView;


    /**
     * 重载系统方法
     *
     * @param isVisibleToUser 视图是否已经对用户可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
//        isCanLoadData();
    }

    /**
     * 出现crash需要保存的数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 如果出现Crash 重新进入页面
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (!savedInstanceState.isEmpty()) {
                setUserVisibleHint(true);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCreateView = true;
        lazyLoad();
    }

    @Override
    protected void createPresenter() {
//        isCreateView = true;
//        lazyLoad();
    }


    /**
     * 等页面完全可见再加载
     */
    protected abstract void lazyLoadCreatePresenter();


    private void onInvisible() {
        isCreateView = false;
    }

    /**
     * 当视图初始化并且对用户可见的时候再加载数据
     */
    protected void lazyLoad() {
        if (isVisible && isCreateView) {
            lazyLoadCreatePresenter();
        }
    }

    @Override
    public void onDestroyView() {
        isCreateView = false;
        isVisible = false;
        super.onDestroyView();
    }
}
