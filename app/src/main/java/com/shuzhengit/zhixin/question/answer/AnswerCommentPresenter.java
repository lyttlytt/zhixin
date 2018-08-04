package com.shuzhengit.zhixin.question.answer;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.AnswerComment;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/28 16:09
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AnswerCommentPresenter extends AnswerContract.Presenter {

    private final AnswerCommentModel mModel;

    public AnswerCommentPresenter(AnswerContract.View view) {
        super(view);
        mModel = new AnswerCommentModel();
    }

    @Override
    public void start() {

    }

    @Override
    void findAnswerComments(int answerId, int page, int pageNum, int userId) {
        mCompositeDisposable.add(
                mModel.fetchAnswerComments(answerId, page, pageNum, userId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<AnswerComment>>() {
                            @Override
                            protected void _onNext(DataContainer<AnswerComment> answerCommentDataContainer) {
                                mView.setMoreData(answerCommentDataContainer);
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.loadMoreFail();
                            }

                            @Override
                            protected void _onCompleted() {
                                super._onCompleted();
                                mView.loadMoreCompleted();
                            }
                        })
        );
    }

    @Override
    void refreshAnswerComment(int answerId, int userId) {
        mCompositeDisposable.add(mModel.fetchAnswerComments(answerId,1,10,userId)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<DataContainer<AnswerComment>>() {
            @Override
            protected void _onNext(DataContainer<AnswerComment> dataContainer) {
                mView.refreshAnswerComment(dataContainer);
            }
        }));
    }

    @Override
    void addAnswerComment(String content, int answerId, int questionId, int userId) {
        mCompositeDisposable.add(
            mModel.addAnswerComment(content, answerId, questionId, userId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<AnswerComment>(mView) {
                    @Override
                    protected void _onNext(AnswerComment answerComment) {
                        mView.addAnswerCommentSuccess();
                    }
                })
        );
    }

    @Override
    void agreeComment(int answerId,int commentId, int userId) {
        mCompositeDisposable.add(mModel.agreeComment(answerId,commentId,userId)
        .compose(RxSchedulersHelper.io_main())
        .subscribeWith(new RxSubscriber<BaseCallModel>() {
            @Override
            protected void _onNext(BaseCallModel callModel) {

            }
        }));
    }

    @Override
    void findAnswerDetailById(int answerId) {
        mCompositeDisposable.add(mModel.fetchAnswerById(answerId)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<QuestionAnswer>() {
            @Override
            protected void _onNext(QuestionAnswer questionAnswer) {
                mView.setAnswerDetail(questionAnswer);
            }
        }));
    }
}
