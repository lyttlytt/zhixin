package com.shuzhengit.zhixin.wenba;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.WenBa;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/8 08:46
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WebBaContract {
    interface View extends BaseView<WebBaContract.Presenter>{
        void refreshSuccess(DataContainer<WenBa> data);
        void refreshFail(String message);
        void refreshCompleted();
        void loadMoreSuccess(DataContainer<WenBa> data);
        void loadMoreFail();
        void loadMoreCompleted();
        void loadNoMore();
    }
    public static abstract class Presenter extends BasePresenter<WebBaContract.View>{
        public Presenter(View view) {
            super(view);
        }
        abstract void findWenBaLists(int userId,int page,int pageSize);
        abstract void refreshWebBaList(int userId,int page,int pageSize);
        abstract void followedWenBa(int userId,int wenBaId);
    }
    interface Model{
        Flowable<BaseCallModel<DataContainer<WenBa>>> fetchWenBaLists(int userId, int page, int pageSize);
        Flowable<BaseCallModel> followedWenBa(int userId,int wenBaId);
    }
}
