package com.shuzhengit.zhixin.question.release_question;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionTag;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 09:55
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ReleaseQuestionPresenter extends ReleaseQuestionContract.Presenter {

    private final ReleaseQuestionModel mReleaseQuestionModel;

    public ReleaseQuestionPresenter(ReleaseQuestionContract.View view) {
        super(view);
        mReleaseQuestionModel = new ReleaseQuestionModel();
    }

    @Override
    public void start() {

    }
    @Override
    void findQuestionTag(String keyword) {
        mCompositeDisposable.add(mReleaseQuestionModel
        .fetchQuestionTag(keyword)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<List<QuestionTag>>() {
            @Override
            protected void _onNext(List<QuestionTag> questionTags) {
                mView.setQuestionKeyWords(questionTags);
            }
        }));
    }




    @Override
    void releaseQuestion(int memberId,String title, String description, List<String> images, int categoryId,
                         List<String> questionTags) {
        mCompositeDisposable.add(mReleaseQuestionModel.releaseQuestion(memberId, title, description, images,
                categoryId,questionTags)
        .compose(RxResultHelper.handleResult())
        .compose(RxSchedulersHelper.io_main())
        .subscribeWith(new RxSubscriber<Question>(mView) {
            @Override
            protected void _onNext(Question question) {
                mView.goReleaseResult();
            }
        }));
    }

}
