package com.shuzhengit.zhixin.index.document.detail;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/16 16:08
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DocumentDetailPresenter extends DocumentDetailContract.Presenter {

    private final DocumentDetailModel mDocumentDetailModel;

    public DocumentDetailPresenter(DocumentDetailContract.View view) {
        super(view);
        mDocumentDetailModel = new DocumentDetailModel();
    }

    @Override
    public void requestDocumentDetail(String esId, int memberId) {
        mCompositeDisposable.add(
                mDocumentDetailModel.fetchDocumentDetail(esId, memberId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<Document>(mView) {
                            @Override
                            protected void _onNext(Document document) {
                                if (document.getIsFavorite() == 0) {
                                    mView.isCollectDocument(false);
                                } else {
                                    mView.isCollectDocument(true);
                                }

                                mView.findDocumentDetailSuccess(document);
                                LogUtils.i("RxSubscriber","_onNext");
                            }
                        })
        );
    }

//    @Override
//    public void checkIsAgreeDocument(int documentId, int userId) {
//        mCompositeDisposable.add(
//                mDocumentDetailModel.checkIsAgreeDocument(documentId, userId)
//                        .compose(RxSchedulersHelper.io_main())
////                .compose(RxResultHelper.handleResult())
//                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
//                            @Override
//                            protected void _onNext(BaseCallModel baseCallModel) {
//
//                            }
//                        })
//        );
//    }

//    @Override
//    public void checkCollectDocument(int documentId, int userId) {
//        mCompositeDisposable.add(mDocumentDetailModel.checkIsCollectDocument(documentId, userId)
//                        .compose(RxSchedulersHelper.io_main())
////        .compose(RxResultHelper.handleResult())
//                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
//                            @Override
//                            protected void _onNext(BaseCallModel baseCallModel) {
//
//                            }
//                        })
//        );
//    }

    @Override
    public void requestRecommendDocument(int documentId) {
        mCompositeDisposable.add(mDocumentDetailModel.fetchRecommendDocument(documentId)
                        .compose(RxSchedulersHelper.io_main())
                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
                            @Override
                            protected void _onNext(BaseCallModel baseCallModel) {

                            }
                        })
        );
    }

    @Override
    public void requestDocumentComments(int documentId, int memberId, int page, int pageSize) {
        mCompositeDisposable.add(mDocumentDetailModel.fetchComments(documentId, memberId, page, pageSize)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Comment>>() {
                    @Override
                    protected void _onNext(List<Comment> comments) {
                        mView.requestDocumentCommentsSuccess(comments);
                    }
                })
        );
    }

    @Override
    public void agreeComment(int commentId, int userId) {
        mCompositeDisposable.add(mDocumentDetailModel.agreeComment(commentId, userId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void _onNext(String comment) {

                    }
                })
        );
    }

    @Override
    public void collectOrCancelDocument(int documentId, int userId) {
        mCompositeDisposable.add(mDocumentDetailModel.collectOrCancelDocument(documentId, userId)
                        .compose(RxSchedulersHelper.io_main())
//        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
                            @Override
                            protected void _onNext(BaseCallModel baseCallModel) {
                                String collect = "收藏成功";
                                if (baseCallModel.message.equals(collect)) {
                                    mView.collectOrCancelDocument(true);
                                } else {
                                    mView.collectOrCancelDocument(false);
                                }
                            }
                        })
        );
    }

    @Override
    public void agreeDocument(int documentId, int userId) {
        mCompositeDisposable.add(mDocumentDetailModel.agreeDocument(documentId, userId)
                .compose(RxSchedulersHelper.io_main())
//        .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel baseCallModel) {
                        mView.isAgreeDocument(true);
                    }
                }));
    }

//    @Override
//    public void secondaryComments(int commentId,int memberId, int page) {
//        mCompositeDisposable.add(mDocumentDetailModel.fetchSecondaryComments(commentId,memberId, page, 10)
//                .compose(RxSchedulersHelper.io_main())
//                .compose(RxResultHelper.handleResult())
//                .subscribeWith(new RxSubscriber<List<Comment>>() {
//                    @Override
//                    protected void _onNext(List<Comment> commentPageContainer) {
//                        mView.requestSecondaryComment(commentPageContainer);
//                    }
//                }));
//    }

    @Override
    public void releaseDocumentComment(String commentContent, int userId, int documentId) {
        mCompositeDisposable.add(mDocumentDetailModel.releaseComment(commentContent, userId, documentId, 0)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<Comment>() {
                    @Override
                    protected void _onNext(Comment comment) {
                        mView.releaseDocumentCommentSuccess(comment);
                    }
                }));
    }

//    @Override
//    public void releaseSecondaryComment(String commentContent, int userId, int documentId, int upperCommentId) {
//        mCompositeDisposable.add(mDocumentDetailModel.releaseComment(commentContent, userId, documentId,
// upperCommentId)
//                .compose(RxSchedulersHelper.io_main())
//                .compose(RxResultHelper.handleResult())
//                .subscribeWith(new RxSubscriber<Comment>() {
//                    @Override
//                    protected void _onNext(Comment comment) {
//                        mView.releaseSecondaryCommentSuccess(comment);
//                    }
//                }));
//    }

    @Override
    public void loadMoreComment(int documentId, int memberId, int page, int pageSize) {
        mCompositeDisposable.add(
                mDocumentDetailModel.fetchComments(documentId, memberId, page, pageSize)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<List<Comment>>() {
                            @Override
                            protected void _onNext(List<Comment> comments) {
                                    mView.loadMoreSuccess(comments);
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.loadMoreFail();
                            }
                        })
        );
    }

    @Override
    public void start() {

    }
}
