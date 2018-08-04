package com.shuzhengit.zhixin.mine.fragment.dynamic;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 22:20
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DynamicQuestionModel implements DynamicQuestionContract.Model {
    @Override
    public Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchDynamicQuestion(int memberId, int page, int pageNum) {

        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<AskWithReply>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<AskWithReply>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchAsks(page, pageNum, memberId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<AskWithReply>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<DataContainer<AskWithReply>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<AskWithReply>> dataContainerBaseCallModel) {
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
