package com.shuzhengit.zhixin.mine.user;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.User;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/22 11:34
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class UserInfoContract {
    interface View extends BaseView<UserInfoContract.Presenter>{
        void findUserSuccess(User user);
        void followSuccess();
        void unFollowSuccess();
    }

    public static abstract class Presenter extends BasePresenter<UserInfoContract.View>{
        public Presenter(View view) {
            super(view);
        }
        abstract void findUser(int memberId,int queryUserId);
        abstract void followUser(int memberId,int followerId);
        abstract void unFollowUser(int memberId,int followerId);
    }
    interface Model{
        Flowable<BaseCallModel<User>> findUserById(int memberId,int queryUserId);
        Flowable<BaseCallModel> follower(int memberId,int followerId);
        Flowable<BaseCallModel> unFollower(int memberId,int followerId);
    }
}
