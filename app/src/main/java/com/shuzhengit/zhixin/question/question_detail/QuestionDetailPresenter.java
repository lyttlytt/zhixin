package com.shuzhengit.zhixin.question.question_detail;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 20:39
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class QuestionDetailPresenter extends QuestionDetailContract.Presenter {
    private static final String TAG = "QuestionDetailPresenter";
    private final QuestionDetailModel mDetailModel;

    public QuestionDetailPresenter(QuestionDetailContract.View view) {
        super(view);
        mDetailModel = new QuestionDetailModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void findQuestionDetail(int questionId,int memberId) {
        mCompositeDisposable.add(mDetailModel.fetchQuestionDetail(questionId,memberId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<Question>(mView) {
                    @Override
                    protected void _onNext(Question dataContainer) {
                        mView.setQuestionDetail(dataContainer);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.failure(message);
                        mView.hideLoading();
                    }
                }));
    }

    @Override
    public void findQuestionAnswers(int page, int pageNum, int questionId, int userId) {
        mCompositeDisposable.add(mDetailModel
                .fetchQuestionAnswer(page, pageNum, questionId, userId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<QuestionAnswer>>() {
                    @Override
                    protected void _onNext(DataContainer<QuestionAnswer> questionAnswerDataContainer) {
                        LogUtils.i(TAG,"_onNext");
                        if (questionAnswerDataContainer.getList().size()==0){
                            mView.loadNoMore();
                        }else {
                            mView.setQuestionAnswers(questionAnswerDataContainer);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        LogUtils.i(TAG,"_onError");
                        mView.loadMoreFail();
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        LogUtils.i(TAG,"_onCompleted");
                        mView.loadMoreComplete();
                    }
                }));
    }

    @Override
    public void agreeAnswer(int questionId, int userId) {
        mCompositeDisposable.add(
                mDetailModel.agreeAnswer(questionId, userId)
                        .compose(RxSchedulersHelper.io_main())
                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
                            @Override
                            protected void _onNext(BaseCallModel callModel) {

                            }
                        })
        );
    }

    @Override
    public void followedQuestion(int likeUserId, int questionId) {
        mCompositeDisposable.add(mDetailModel.followerQuestion(likeUserId, questionId)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {

                    }
                })

        );
    }

    @Override
    public void refreshAnswer(int questionId, int userId) {
        mCompositeDisposable.add(mDetailModel.fetchQuestionAnswer(1,10,questionId,userId)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<DataContainer<QuestionAnswer>>() {
            @Override
            protected void _onNext(DataContainer<QuestionAnswer> answerDataContainer) {
                mView.refreshQuestionAnswer(answerDataContainer);
            }
        }));
    }
}
