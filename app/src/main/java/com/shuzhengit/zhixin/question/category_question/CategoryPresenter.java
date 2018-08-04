//package com.shuzhengit.zhixin.question.category_question;
//
//import com.library.rx.RxResultHelper;
//import com.library.rx.RxSchedulersHelper;
//import com.shuzhengit.zhixin.bean.DataContainer;
//import com.shuzhengit.zhixin.bean.Question;
//import com.shuzhengit.zhixin.rx.RxSubscriber;
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/9/26 16:00
// * E-mail:yuancongbin@gmail.com
// * <p>
// * 功能描述:
// */
//
//public class CategoryPresenter extends CategoryQuestionContract.Presenter {
//
//    private final CategoryModel mModel;
//
//    public CategoryPresenter(CategoryQuestionContract.View view) {
//        super(view);
//        mModel = new CategoryModel();
//    }
//
//    @Override
//    public void start() {
//
//    }
//
//    @Override
//    void findQuestions(int page, int pageNum, int categoryId) {
//        mCompositeDisposable.add(
//                mModel.fetchQuestions(page, pageNum, categoryId)
//                .compose(RxSchedulersHelper.io_main())
//                .compose(RxResultHelper.handleResult())
//                .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
//                    @Override
//                    protected void _onNext(DataContainer<Question> dataContainer) {
//                        mView.loadMoreQuestions(dataContainer);
//                    }
//
//                    @Override
//                    protected void _onCompleted() {
//                        super._onCompleted();
//                        mView.loadMoreCompleted();
//                    }
//
//                    @Override
//                    protected void _onError(String message) {
//                        super._onError(message);
//                        mView.failure(message);
//                        mView.loadMoreFail();
//                    }
//                })
//        );
//    }
//
//    @Override
//    void refreshQuestion(int categoryId) {
//        mCompositeDisposable.add(
//                mModel.fetchQuestions(1, 10, categoryId)
//                        .compose(RxSchedulersHelper.io_main())
//                        .compose(RxResultHelper.handleResult())
//                        .subscribeWith(new RxSubscriber<DataContainer<Question>>() {
//                            @Override
//                            protected void _onNext(DataContainer<Question> dataContainer) {
//                                mView.refreshQuestions(dataContainer);
//                            }
//
//                            @Override
//                            protected void _onCompleted() {
//                                super._onCompleted();
//                                mView.refreshCompleted();
//                            }
//
//                            @Override
//                            protected void _onError(String message) {
//                                super._onError(message);
//                                mView.failure(message);
//                                mView.refreshCompleted();
//                            }
//                        })
//        );
//    }
//}
