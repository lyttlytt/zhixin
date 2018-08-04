package com.shuzhengit.zhixin.mine.question;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 22:37
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineQuestionPresenter extends MineQuestionContract.Presenter {

    private final MineQuestionModel mModel;

    public MineQuestionPresenter(MineQuestionContract.View view) {
        super(view);
        mModel = new MineQuestionModel();
    }

    @Override
    public void start() {

    }

    @Override
    void findMineReleaseQuestionByUserId(int userId, int page, int pageNum) {
        mCompositeDisposable.add(mModel.fetchMineReleaseQuestionByUserId(userId, page, pageNum)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
            @Override
            protected void _onNext(DataContainer<Question> dataContainer) {
                if (dataContainer.getList().size()==0){
                    mView.loadNoMore();
                }else {
                    mView.setQuestions(dataContainer);
                }
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
        }));
    }

    @Override
    void findMineReplyQuestionByUserId(int userId, int page, int pageNum) {
        mCompositeDisposable.add(mModel.fetchMineReplyQuestionByUserId(userId, page, pageNum)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
                    @Override
                    protected void _onNext(DataContainer<Question> dataContainer) {
                        if (dataContainer.getList().size()==0){
                            mView.loadNoMore();
                        }else {
                            mView.setQuestions(dataContainer);
                        }
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
                }));
    }

    @Override
    void findMienFollowQuestionByUserId(int userId, int page, int pageNum) {
        mCompositeDisposable.add(mModel.fetchMineFollowQuestionByUserId(userId, page, pageNum)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
                    @Override
                    protected void _onNext(DataContainer<Question> dataContainer) {
                        if (dataContainer.getList().size()==0){
                            mView.loadNoMore();
                        }else {
                            mView.setQuestions(dataContainer);
                        }
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
                }));
    }

    @Override
    void refreshMineReleaseQuestionByUserId(int userId) {
        mCompositeDisposable.add(mModel.fetchMineReleaseQuestionByUserId(userId,1,10)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
            @Override
            protected void _onNext(DataContainer<Question> dataContainer) {
                mView.refreshData(dataContainer);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.failure(message);
                mView.refreshCompleted();
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.refreshCompleted();
            }
        }));
    }

    @Override
    void refreshMineReplyQuestionByUserId(int userId) {
        mCompositeDisposable.add(mModel.fetchMineReplyQuestionByUserId(userId,1,10)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
                    @Override
                    protected void _onNext(DataContainer<Question> dataContainer) {
                        mView.refreshData(dataContainer);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.failure(message);
                        mView.refreshCompleted();
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.refreshCompleted();
                    }
                }));
    }

    @Override
    void refreshMineFollowQuestionByUserId(int userId) {
        mCompositeDisposable.add(mModel.fetchMineFollowQuestionByUserId(userId,1,10)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
                    @Override
                    protected void _onNext(DataContainer<Question> dataContainer) {
                        mView.refreshData(dataContainer);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.failure(message);
                        mView.refreshCompleted();
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.refreshCompleted();
                    }
                }));
    }
}
