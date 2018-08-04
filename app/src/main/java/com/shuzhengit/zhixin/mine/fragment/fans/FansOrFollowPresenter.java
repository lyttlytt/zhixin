package com.shuzhengit.zhixin.mine.fragment.fans;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/23 16:10
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class FansOrFollowPresenter extends FansOrFollowContract.Presenter {

    private final FansOrFollowModel mModel;

    public FansOrFollowPresenter(FansOrFollowContract.View view) {
        super(view);
        mModel = new FansOrFollowModel();
    }

    @Override
    public void start() {

    }

    @Override
    void findFans(int memberId, int page) {
        mCompositeDisposable.add(mModel.findFansByPage(memberId, page)
                .compose(RxResultHelper.handleResult())
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<DataContainer<User>>() {
                    @Override
                    protected void _onNext(DataContainer<User> dataContainer) {
                        if (dataContainer.getList().size()!=0){
                            mView.addMoreFollows(dataContainer);
                        }else {
                            mView.loadNoMore();
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.loadMoreFail();
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.loadMoreCompleted();
                    }
                }));
    }

    @Override
    void findFollows(int memberId, int page) {
        mCompositeDisposable.add(mModel.findFollowByPage(memberId, page)
                .compose(RxResultHelper.handleResult())
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<DataContainer<User>>() {
                    @Override
                    protected void _onNext(DataContainer<User> dataContainer) {
                        if (dataContainer.getList().size()==0){
                            mView.loadNoMore();
                        }else {
                            mView.findFollowSuccess(dataContainer);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.loadMoreFail();
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.loadMoreCompleted();
                    }
                }));
    }

    @Override
    void unFollow(int memberId, int followerId) {
        mCompositeDisposable.add(mModel.unFollowUser(memberId, followerId)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {

                    }
                })
        );
    }

}
