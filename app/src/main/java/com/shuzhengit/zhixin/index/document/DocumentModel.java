package com.shuzhengit.zhixin.index.document;

import com.library.bean.BaseCallModel;
import com.library.util.SPUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CheckUser;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/14 15:50
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DocumentModel implements DocumentContract.Model {
    @Override
    public Flowable<BaseCallModel<List<Document>>> requestDocuments(String columnCode, int page) {
        if (columnCode.equals("tuijian")) {

            return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Document>>>() {
                @Override
                public void subscribe(FlowableEmitter<BaseCallModel<List<Document>>> subscriber) throws Exception {
                    User user = CheckUser.checkUserIsExists();
                    int schoolId;
                    if (user!=null){
                        schoolId=  user.getSchoolId();
                    }else {
                        schoolId = (int) SPUtils.get(APP.getInstance(), CacheKeyManager.ANONYMOUS_SCHOOL_ID, 0);
                    }
                    HttpProtocol.getApi().findRecommendDocument(page,10)
                            .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Document>>>(subscriber));
                }
            },BackpressureStrategy.BUFFER);
        }else if (columnCode.equals("zhoubian")){
            return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Document>>>() {
                @Override
                public void subscribe(FlowableEmitter<BaseCallModel<List<Document>>> subscriber) throws Exception {
                    User user = CheckUser.checkUserIsExists();
                    int schoolId;
                    if (user!=null){
                        schoolId=  user.getSchoolId();
                    }else {
                        schoolId = (int) SPUtils.get(APP.getInstance(), CacheKeyManager.ANONYMOUS_SCHOOL_ID, 0);
                    }
                    HttpProtocol.getApi().findZhouBianDocumentBySchoolId(schoolId,page,10)
                            .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Document>>>(subscriber));
                }
            },BackpressureStrategy.BUFFER);
        }else if (columnCode.equals("benxiao")){
            return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Document>>>() {
                @Override
                public void subscribe(FlowableEmitter<BaseCallModel<List<Document>>> subscriber) throws Exception {
                    User user = CheckUser.checkUserIsExists();
                    int schoolId;
                    if (user!=null){
                        schoolId=  user.getSchoolId();
                    }else {
                        schoolId = (int) SPUtils.get(APP.getInstance(), CacheKeyManager.ANONYMOUS_SCHOOL_ID, 0);
                    }
                    HttpProtocol.getApi().findDocumentBySchoolId(schoolId,page,10)
                            .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Document>>>(subscriber));
                }
            },BackpressureStrategy.BUFFER);
        }else {
            return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Document>>>() {
                @Override
                public void subscribe(FlowableEmitter<BaseCallModel<List<Document>>> subscriber) throws Exception {
                    HttpProtocol.getApi().findDocumentByColumnId(columnCode, page, 10)
                            .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Document>>>(subscriber));
//                                    new DisposableSubscriber<BaseCallModel<List<Document>>>() {
//
//                                @Override
//                                public void onNext(BaseCallModel<List<Document>> listBaseCallModel) {
//                                    subscriber.onNext(listBaseCallModel);
//                                }
//
//                                @Override
//                                public void onError(Throwable t) {
//                                    if (!subscriber.isCancelled()) {
//                                        subscriber.onError(t);
//                                    }
//                                }
//
//                                @Override
//                                public void onComplete() {
//                                    subscriber.onComplete();
//                                }
//                            });
                }
            }, BackpressureStrategy.BUFFER);
        }
    }
}
