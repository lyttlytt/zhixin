//package com.shuzhengit.zhixin.question.category_question;
//
//import com.library.bean.BaseCallModel;
//import com.shuzhengit.zhixin.bean.DataContainer;
//import com.shuzhengit.zhixin.bean.Question;
//import com.shuzhengit.zhixin.http.HttpProtocol;
//
//import io.reactivex.BackpressureStrategy;
//import io.reactivex.Flowable;
//import io.reactivex.FlowableEmitter;
//import io.reactivex.FlowableOnSubscribe;
//import io.reactivex.subscribers.DisposableSubscriber;
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/9/26 16:00
// * E-mail:yuancongbin@gmail.com
// * <p>
// * 功能描述:
// */
//
//public class CategoryModel implements CategoryQuestionContract.Model {
//    @Override
//    public Flowable<BaseCallModel<DataContainer<Question>>> fetchQuestions(int page, int pageNum, int categoryId) {
//        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<Question>>>() {
//            @Override
//            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<Question>>> e) throws Exception {
//                HttpProtocol.getApi()
//                        .findQuestionByCategoryId(page, pageNum, categoryId)
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<Question>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<Question>> dataContainerBaseCallModel) {
//                                e.onNext(dataContainerBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                e.onError(t);
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                e.onComplete();
//                            }
//                        });
//            }
//        }, BackpressureStrategy.BUFFER);
//    }
//}
