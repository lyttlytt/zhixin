package com.shuzhengit.zhixin.question.release_question;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionCategory;
import com.shuzhengit.zhixin.bean.QuestionTag;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 09:53
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ReleaseQuestionContract {
    interface View extends BaseView<ReleaseQuestionContract.Presenter>{
        void goReleaseResult();
        void setQuestionKeyWords(List<QuestionTag> tags);

    }

    public static abstract class Presenter extends BasePresenter<ReleaseQuestionContract.View>{
        public Presenter(View view) {
            super(view);
        }

        abstract void findQuestionTag(String keyword);
        abstract void releaseQuestion(int memberId,String title, String description, List<String> images,int
                categoryId,List<String> questionTags);
    }

    interface Model{
        Flowable<BaseCallModel<Question>> releaseQuestion(int memberId, String title, String description, List<String>
                images, int categoryId, List<String> questionTags);
        Flowable<BaseCallModel<DataContainer<QuestionCategory>>> fetchQuestionCategory();
        Flowable<BaseCallModel<List<QuestionTag>>> fetchQuestionTag(String keyword);
    }
}
