package com.shuzhengit.zhixin.wenba.mine;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.WenBa;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/15 14:26
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineFollowWenBaContract {
    interface View extends BaseView<MineFollowWenBaContract.Presenter>{
        void onRefreshSuccess(DataContainer<WenBa> dataContainer);
        void onRefreshFail();
        void onRefreshCompleted();
        void onLoadMoreSuccess(DataContainer<WenBa> dataContainer);
        void onLoadMoreFail();
        void onLoadNoMore();
        void onLoadMoreCompleted();
    }
    public static abstract class Presenter extends BasePresenter<MineFollowWenBaContract.View>{
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
        abstract void findMineFollowWenBa(int page,int pageSize,int userId);
        abstract void refreshFollowWenBa(int page,int pageSize,int userId);
    }
    interface Model{
        Flowable<BaseCallModel<DataContainer<WenBa>>> fetchFollowWenBas(int page,int pageSize,int userId);
    }
}
