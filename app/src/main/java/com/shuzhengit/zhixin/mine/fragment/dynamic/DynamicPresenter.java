package com.shuzhengit.zhixin.mine.fragment.dynamic;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.Dynamic;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 10:57
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DynamicPresenter extends DynamicContract.Presenter {

    private final DynamicModel mModel;

    public DynamicPresenter(DynamicContract.View view) {
        super(view);
        mModel = new DynamicModel();
    }

    @Override
    public void start() {
    }

    @Override
    public void findDynamics(int memberId, int page, int pageNum) {
        mCompositeDisposable.add(mModel.fetchDynamics(memberId, page, pageNum)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Dynamic>>() {
                    @Override
                    protected void _onNext(List<Dynamic> dynamics) {
                        if (dynamics.size() == 0) {
                            mView.loadMoreNoData();
                        } else {
                            mView.setDynamics(dynamics);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.loadMoreFail(message);
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.loadMoreCompleted();
                    }
                }));
    }
}
