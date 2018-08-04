package com.shuzhengit.zhixin.wenba.mine;

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
 * Author：袁从斌 on 2017/11/15 15:41
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineAskModel implements MineReleaseContract.Model {
    @Override
    public Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchAsks(int page, int pageSize, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<AskWithReply>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<AskWithReply>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchAsks(page, pageSize, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<AskWithReply>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<AskWithReply>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<AskWithReply>> dataContainerBaseCallModel) {
//                                e.onNext(dataContainerBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                if (!e.isCancelled()) {
//                                    e.onError(t);
//                                }
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
