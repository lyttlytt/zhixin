package com.library.base;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/6/26 09:17
 * E-mail:yuancongbin@gmail.com
 */

public abstract class BaseHasXRecyclerViewPresenter<V extends BaseView> extends BasePresenter<V> {


    public BaseHasXRecyclerViewPresenter(V view) {
        super(view);
    }

    /**
     * XRecyclerView的下拉刷新
     * XRecyclerView pullToRefresh
     */
    public abstract void onRefresh();

    /**
     * XRecyclerView的加载更多
     * XRecyclerView load more
     */
    public abstract void onLoadMore();


}
