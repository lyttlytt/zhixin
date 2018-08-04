package com.shuzhengit.zhixin.question;

import com.library.rx.RxBus2;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.EventCodeUtils;

import io.reactivex.functions.Predicate;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/18 11:13
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class QuestionPresenter extends QuestionContract.Presenter {

    private static final String TAG = "QuestionPresenter";
    private final QuestionModel mQuestionModel;

    public QuestionPresenter(QuestionContract.View view) {
        super(view);
        mQuestionModel = new QuestionModel();
    }

    @Override
    public void start() {
        mCompositeDisposable.add(RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.RELEASE_REFRESH_QUESTION);
                    }
                })
                .subscribeWith(new RxSubscriber<EventType>() {
                    @Override
                    protected void _onNext(EventType eventType) {
                        refreshQuestion(1);
                        LogUtils.d(TAG,"rxbus toFlowable refresh qeustion" );
                    }
                }));
    }

    @Override
    void findQuestions(int page, int pageNum, int categoryId) {
        mCompositeDisposable.add(mQuestionModel.fetchQuestions(page, pageNum, categoryId)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
            @Override
            protected void _onNext(DataContainer<Question> dataContainer) {
                mView.loadMoreQuestions(dataContainer);
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.loadMoreCompleted();
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.failure(message);
                mView.loadMoreFail();
            }
        }));
    }

    @Override
    void refreshQuestion(int categoryId) {
        mCompositeDisposable.add(
                mQuestionModel.fetchQuestions(1, 10, categoryId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
                            @Override
                            protected void _onNext(DataContainer<Question> dataContainer) {
                                mView.refreshQuestions(dataContainer);
                            }

                            @Override
                            protected void _onCompleted() {
                                super._onCompleted();
                                mView.refreshCompleted();
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.failure(message);
                                mView.refreshCompleted();
                            }
                        })
        );
    }
}
