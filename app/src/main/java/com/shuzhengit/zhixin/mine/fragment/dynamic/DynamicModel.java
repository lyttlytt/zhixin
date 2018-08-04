package com.shuzhengit.zhixin.mine.fragment.dynamic;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Dynamic;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 10:58
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DynamicModel implements DynamicContract.Model {
    @Override
    public Flowable<BaseCallModel<List<Dynamic>>> fetchDynamics(int memberId, int page, int pageNum) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Dynamic>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Dynamic>>> e) throws Exception {
                HttpProtocol.getApi()
                        .findDynamics(memberId + "", page, pageNum)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Dynamic>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<List<Dynamic>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Dynamic>> listBaseCallModel) {
//                                e.onNext(listBaseCallModel);
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
