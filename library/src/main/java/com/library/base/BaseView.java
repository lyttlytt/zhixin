package com.library.base;


/**
 * Created by 袁从斌-where on 2017/1/3.
 * view的基类 把抽取一些公用的方法
 */

public interface  BaseView<P extends BasePresenter> {
    /**
     * 显示加载中动画
     * show loading status
     */
    abstract void showLoading();

    /**
     * 隐藏加载中动画
     * hide loading status
     */
    abstract void hideLoading();

//    /**
//     * 网络请求失败
//     * network request failed info
//     */
//    abstract void failure(View view, String msg);

    /**
     * 网络请求失败
     * network request failed info
     */
    abstract void failure(String msg);

    /**
     * toast提示
     * @param resId 资源id
     */
    abstract void failure(int resId);

    /**
     * 与Presenter关联
     * depend view
     */
     abstract void setPresenter(P presenter);


    /**
     * 网络请求失败的返回的信息
     * network refresh with loadMore   on error message
     * @param message error info
     */
    abstract void requestDataOnError(String message);
}
