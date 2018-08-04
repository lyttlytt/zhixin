package com.shuzhengit.zhixin.wenba.detail.reply;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.AskComment;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/15 20:35
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ReplyPresenter extends ReplyContract.Presenter {

    private final ReplyModel mModel;

    public ReplyPresenter(ReplyContract.View view) {
        super(view);
        mModel = new ReplyModel();
    }

    @Override
    public void start() {

    }

    @Override
    void comment(String content, int askId, int wenBaId, int replayUserId) {
        mCompositeDisposable.add(
                mModel.postComment(content, askId, wenBaId, replayUserId)
                        .compose(RxSchedulersHelper.io_main())
                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
                            @Override
                            protected void _onNext(BaseCallModel callModel) {
                                mView.commentSuccess();
                            }
                        })
        );
    }

    @Override
    void findAskById(int askId, int userId) {
        mCompositeDisposable.add(
                mModel.findAskWithReplyById(askId, userId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<AskWithReply>() {
                            @Override
                            protected void _onNext(AskWithReply askWithReply) {
                                mView.setAskWithReplyInfo(askWithReply);
                            }
                        })
        );
    }

    @Override
    void agreeAsk(int askId, int userId) {
        mCompositeDisposable.add(
                mModel.agreeAsk(askId, userId)
                        .compose(RxSchedulersHelper.io_main())
                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
                            @Override
                            protected void _onNext(BaseCallModel callModel) {

                            }
                        })
        );
    }

    @Override
    void findCommentById(int page, int pageSize, int userId, int askId) {
        mCompositeDisposable.add(
                mModel.fetchComments(page, pageSize, userId, askId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<AskComment>>() {
                            @Override
                            protected void _onNext(DataContainer<AskComment> dataContainer) {
                                if (dataContainer.getList().size()!=0){
                                    mView.addMoreComments(dataContainer);
                                }else {
                                    mView.addMoreComments(dataContainer);
                                }
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.failure(message);
                                mView.loadMoreFail();
                            }

                            @Override
                            protected void _onCompleted() {
                                super._onCompleted();
                                mView.loadMoreComplete();
                            }
                        })
        );
    }

    @Override
    void refreshComments(int page, int pageSize, int userId, int askId) {
        mCompositeDisposable.add(
                mModel.fetchComments(page, pageSize, userId, askId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<AskComment>>() {
                            @Override
                            protected void _onNext(DataContainer<AskComment> dataContainer) {
                                mView.refreshComments(dataContainer);
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.failure(message);
                            }
                        })
        );
    }

    @Override
    void agreeComment(int askId, int commentId, int userId) {
        mCompositeDisposable.add(
                mModel.agreeComment(askId, commentId, userId)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {

                    }
                })

        );
    }


}
