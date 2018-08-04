package com.shuzhengit.zhixin.wenba.mine;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/15 15:17
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineReleaseContract {
    interface View extends BaseView<MineReleaseContract.Presenter> {
        void onRefreshSuccess(DataContainer<AskWithReply> dataContainer);
        void onRefreshFail();
        void onRefreshCompleted();
        void onLoadMoreSuccess(DataContainer<AskWithReply> dataContainer);
        void onLoadMoreFail();
        void onLoadNoMore();
        void onLoadMoreCompleted();
    }

    public static abstract class Presenter extends BasePresenter<MineReleaseContract.View> {
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
        abstract void findMineAsks(int page,int pageSize,int userId);
        abstract void refreshAsks(int page,int pageSize,int userId);
    }

    interface Model {
        Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchAsks(int page, int pageSize, int userId);

    }
}
