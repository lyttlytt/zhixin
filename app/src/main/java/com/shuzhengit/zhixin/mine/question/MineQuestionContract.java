package com.shuzhengit.zhixin.mine.question;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Question;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 22:29
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineQuestionContract {
    interface View extends BaseView<MineQuestionContract.Presenter> {
        void loadMoreCompleted();
        void setQuestions(DataContainer<Question> dataContainer);
        void loadMoreFail();
        void loadNoMore();

        void refreshCompleted();
        void refreshData(DataContainer<Question> refreshData);
    }

    public static abstract class Presenter extends BasePresenter<MineQuestionContract.View> {
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
        abstract void findMineReleaseQuestionByUserId(int userId, int page, int pageNum);

        abstract void findMineReplyQuestionByUserId(int userId, int page, int pageNum);

        abstract void findMienFollowQuestionByUserId(int userId, int page, int pageNum);

        abstract void refreshMineReleaseQuestionByUserId(int userId);

        abstract void refreshMineReplyQuestionByUserId(int userId);

        abstract void refreshMineFollowQuestionByUserId(int userId);
    }

    interface Model {
        Flowable<BaseCallModel<DataContainer<Question>>> fetchMineReleaseQuestionByUserId(int userId, int page, int
                pageNum);

        Flowable<BaseCallModel<DataContainer<Question>>> fetchMineReplyQuestionByUserId(int userId,int page,int
                pageNum);

        Flowable<BaseCallModel<DataContainer<Question>>> fetchMineFollowQuestionByUserId(int userId,int page,int
                pageNum);

    }
}
