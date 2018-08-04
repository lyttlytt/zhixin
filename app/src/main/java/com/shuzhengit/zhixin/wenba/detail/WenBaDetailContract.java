package com.shuzhengit.zhixin.wenba.detail;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.WenBa;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/14 10:33
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WenBaDetailContract {
    interface View extends BaseView<WenBaDetailContract.Presenter>{
        void setWenBaDetail(WenBa wenBa);
        void addAskWithReplys(DataContainer<AskWithReply> dataContainer);
        void loadMoreFail();
        void loadMoreSuccess(DataContainer<AskWithReply> dataContainer);
        void loadNoMore();
        void refreshData(DataContainer<AskWithReply> dataContainer);
        void refreshCompleted();
    }
    public static abstract class Presenter extends BasePresenter<WenBaDetailContract.View>{
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
        abstract void findWenBaDetailById(int wenBaId,int userId);
        abstract void findAskWithReplysOnNew(int page,int pageSize,int wenBaId,int userId);
        abstract void findAskWithReplysOnHot(int page,int pageSize,int wenBaId,int userId);
        abstract void refreshAskWithReplyOnNew(int page,int pageSize,int wenBaId,int userId);
        abstract void refreshAskWithReplyOnHot(int page,int pageSize,int wenBaId,int userId);
        abstract void followedWenBa(int userId,int wenBaId);
        abstract void agreeAsk(int askId,int userId);

    }
    interface Model {
        Flowable<BaseCallModel<WenBa>> fetchWenBa(int wenBaId,int userId);
        Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchAskAndReplysOnNew(int page,int pageSize,int wenBaId,int userId);
        Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchAskAndReplysOnHot(int page,int pageSize,int wenBaId,
                                                                                  int userId);
        Flowable<BaseCallModel> followedWenBa(int userId,int wenBaId);
        Flowable<BaseCallModel> agreeAsk(int askId,int userId);
    }
}
