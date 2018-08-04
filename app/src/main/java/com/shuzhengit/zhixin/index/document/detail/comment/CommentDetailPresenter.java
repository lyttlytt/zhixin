package com.shuzhengit.zhixin.index.document.detail.comment;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/7 09:21
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class CommentDetailPresenter extends CommentDetailContract.Presenter {

    private final CommentDetailModel mCommentDetailModel;

    public CommentDetailPresenter(CommentDetailContract.View view) {
        super(view);
        mCommentDetailModel = new CommentDetailModel();
    }

    @Override
    public void start() {

    }

    @Override
    void requestMasterComment( int masterCommentId,int memberId) {
        mCompositeDisposable.add(mCommentDetailModel.fetchMasterComment(masterCommentId,memberId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<Comment>(mView) {
                    @Override
                    protected void _onNext(Comment comment) {
                        mView.findMasterCommentSuccess(comment);
                    }
                }));
    }

    @Override
    void requestSecondaryComments(int masterId, int memberId,int page, int pageSize) {
        mCompositeDisposable.add(
                mCommentDetailModel.fetchSecondaryComments(masterId, memberId,page, pageSize)
                        .compose(RxResultHelper.handleResult())
                        .compose(RxSchedulersHelper.io_main())
                        .subscribeWith(new RxSubscriber<List<Comment>>() {
                            @Override
                            protected void _onNext(List<Comment> comments) {
                                mView.findSecondaryCommentSuccess(comments);
                            }
                        })
        );
    }

    @Override
    void requestSecondaryMoreComments(int masterId, int memberId,int page, int pageSize) {
        mCompositeDisposable.add(mCommentDetailModel.fetchSecondaryComments(masterId,memberId, page, pageSize)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Comment>>() {
                    @Override
                    protected void _onNext(List<Comment> comments) {
                        if (comments.size() == 0) {
                            mView.loadNoMore();
                        } else {
                            mView.loadMoreSuccess(comments);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.loadMoreFail();
                    }
                }));
    }

    @Override
    void releaseComment(String commentContent, int userId,int documentId, int masterCommentId) {
        mCompositeDisposable.add(
                mCommentDetailModel.releaseComment(commentContent, userId,documentId, masterCommentId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<Comment>(mView) {
                    @Override
                    protected void _onNext(Comment comment) {
                        mView.releaseCommentSuccess(comment);
                    }
                })
        );
    }
}
