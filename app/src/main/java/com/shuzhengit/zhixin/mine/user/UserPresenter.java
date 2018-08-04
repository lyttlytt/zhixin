package com.shuzhengit.zhixin.mine.user;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/22 14:56
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class UserPresenter extends UserInfoContract.Presenter {

    private final UserModel mModel;

    public UserPresenter(UserInfoContract.View view) {
        super(view);
        mModel = new UserModel();
    }

    @Override
    public void start() {

    }

    @Override
    void findUser(int memberId,int queryUserId) {
        mCompositeDisposable.add(mModel.findUserById(memberId,queryUserId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<User>() {
                    @Override
                    protected void _onNext(User user) {
                        mView.findUserSuccess(user);
                    }
                }));
    }

    @Override
    void followUser(int memberId, int followerId) {
        mCompositeDisposable.add(mModel.follower(memberId, followerId)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel stringBaseCallModel) {
                        if (stringBaseCallModel.code == 200) {
                            mView.failure(stringBaseCallModel.getMessage());
                            mView.followSuccess();
                        }
                    }
                }));
    }

    @Override
    void unFollowUser(int memberId, int followerId) {
        mCompositeDisposable.add(mModel.unFollower(memberId, followerId)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel stringBaseCallModel) {
                        if (stringBaseCallModel.code == 200) {
                            mView.failure(stringBaseCallModel.getMessage());
                            mView.unFollowSuccess();
                        }
                    }
                }));
    }
}
