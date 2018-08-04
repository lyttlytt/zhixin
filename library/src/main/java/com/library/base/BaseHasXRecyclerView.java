package com.library.base;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/6/26 08:50
 * E-mail:yuancongbin@gmail.com
 */

public interface BaseHasXRecyclerView<P extends BasePresenter> extends BaseView<P> {
    /**
     * 如果使用了XRecyclerView的下拉刷新,完成之后要设置completed
     * XRecyclerView refresh completed
     */
    abstract void refreshCompleted();


    /**
     * 如果使用了XRecyclerView的加载更多,完成之后要设置completed
     * loadMore on completed
     */
    abstract void loadMoreCompleted();

}
