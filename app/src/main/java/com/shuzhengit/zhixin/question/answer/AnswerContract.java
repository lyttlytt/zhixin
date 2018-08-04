package com.shuzhengit.zhixin.question.answer;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AnswerComment;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.QuestionAnswer;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/28 12:45
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AnswerContract {
    interface View extends BaseView<AnswerContract.Presenter>{
        void loadMoreCompleted();
        void loadNoMore();
        void loadMoreFail();
        void setMoreData(DataContainer<AnswerComment> dataContainer);
        void refreshAnswerComment(DataContainer<AnswerComment> dataContainer);
        void addAnswerCommentSuccess();
        void setAnswerDetail(QuestionAnswer questionAnswer);
    }
    public static abstract class Presenter extends BasePresenter<AnswerContract.View>{

        public Presenter(View view) {
            super(view);
        }
        abstract void findAnswerComments(int answerId,int page,int pageNum,int userId);
        abstract void refreshAnswerComment(int answerId,int userId);
        abstract void addAnswerComment(String content,int answerId,int questionId,int userId);
        abstract void agreeComment(int answerId,int commentId,int userId);
        abstract void findAnswerDetailById(int answerId);
    }
    interface Model{
        Flowable<BaseCallModel<DataContainer<AnswerComment>>> fetchAnswerComments(int answerId,int page,int pageNum,
                                                                                  int userId);
        Flowable<BaseCallModel<AnswerComment>> addAnswerComment(String content,int answerId,int questionId,int userId);

        Flowable<BaseCallModel> agreeComment(int answerId,int commentId,int userId);

        Flowable<BaseCallModel<QuestionAnswer>> fetchAnswerById(int answerId);
    }
}
