package com.shuzhengit.zhixin.wenba.mine;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/15 17:18
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class UnReplyModel implements UnReplyContract.Model {
    @Override
    public Flowable<BaseCallModel<DataContainer<WenBa>>> fetchUnReplys(int page, int pageSize, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<WenBa>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<WenBa>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchAdminUnReplys(page, pageSize, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<WenBa>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<WenBa>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<WenBa>> dataContainerBaseCallModel) {
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
            }
        }, BackpressureStrategy.BUFFER);
    }
}
