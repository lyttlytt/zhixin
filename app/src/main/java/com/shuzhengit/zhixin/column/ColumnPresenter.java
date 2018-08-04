package com.shuzhengit.zhixin.column;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/21 14:12
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

 class ColumnPresenter extends ColumnContract.Presenter {

    private final ColumnModel mColumnModel;

     ColumnPresenter(ColumnContract.View view) {
        super(view);
        mColumnModel = new ColumnModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void requestMineFollowColumn(int memberId) {
        mCompositeDisposable.add(mColumnModel.fetchMineFollowColumn(memberId)
        .compose(RxResultHelper.handleResult())
        .compose(RxSchedulersHelper.io_main())
        .subscribeWith(new RxSubscriber<List<Column>>() {
            @Override
            protected void _onNext(List<Column> columns) {
                mView.findMineFollowColumnSuccess(columns);
            }
        }));
    }

    @Override
    public void requestRecommendColumn(int memberId) {
        mCompositeDisposable.add(mColumnModel.fetchRecommendColumn(memberId)
        .compose(RxResultHelper.handleResult())
        .compose(RxSchedulersHelper.io_main())
        .subscribeWith(new RxSubscriber<List<Column>>() {
            @Override
            protected void _onNext(List<Column> columns) {
                mView.findRecommendColumnSuccess(columns);
            }
        }));
    }
}
