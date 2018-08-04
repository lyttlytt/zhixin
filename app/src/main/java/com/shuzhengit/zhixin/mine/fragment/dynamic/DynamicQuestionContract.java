package com.shuzhengit.zhixin.mine.fragment.dynamic;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 10:50
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DynamicQuestionContract {
    interface View extends BaseView<DynamicQuestionContract.Presenter>{
        void setDynamicQuestion(DataContainer<AskWithReply> dynamics);
        void loadMoreCompleted();
        void loadMoreFail(String message);
        void loadMoreNoData();
    }
    public static abstract class Presenter extends BasePresenter<DynamicQuestionContract.View>{
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
        public abstract void findDynamicQuestion(int memberId,int page,int pageNum);
    }
    interface Model{
        Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchDynamicQuestion(int memberId, int page, int pageNum);
    }
}
