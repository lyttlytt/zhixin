package com.shuzhengit.zhixin.question.question_detail;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionAnswer;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 19:18
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class QuestionDetailContract {
    interface View extends BaseView<QuestionDetailContract.Presenter>{
        void setQuestionDetail(Question questionDetail);
        void setQuestionAnswers(DataContainer<QuestionAnswer> answers);
        void refreshQuestionAnswer(DataContainer<QuestionAnswer> answers);
        void loadMoreFail();
        void loadMoreComplete();
        void loadNoMore();
    }
    public static abstract class Presenter extends BasePresenter<QuestionDetailContract.View>{

        public Presenter(View view) {
            super(view);
        }
        public abstract void findQuestionDetail(int questionId,int memberId);
        public abstract void findQuestionAnswers(int page,int pageNum,int questionId,int userId);
        public abstract void agreeAnswer(int answerId,int userId);
        public abstract void followedQuestion(int likeUserId,int questionId);
        public abstract void refreshAnswer(int questionId,int userId);
    }
    interface Model{
        Flowable<BaseCallModel<Question>> fetchQuestionDetail(int questionId,int memberId);
        Flowable<BaseCallModel<DataContainer<QuestionAnswer>>> fetchQuestionAnswer(int page,int pageNum,int
                questionId,int userId);

        Flowable<BaseCallModel> agreeAnswer(int answerId,int userId);

        Flowable<BaseCallModel> followerQuestion(int likeUserId,int questionId);
    }

}
