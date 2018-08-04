package com.shuzhengit.zhixin.mine.fragment.fans;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.User;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/23 15:49
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class FansOrFollowContract {
    interface View extends BaseView<FansOrFollowContract.Presenter>{
        void findFansSuccess(DataContainer<User> dataContainer);
        void findFollowSuccess(DataContainer<User> dataContainer);
        void addMoreFollows(DataContainer<User> dataContainer);
        void addMoreFans(DataContainer<User> dataContainer);
        void loadMoreFail();
        void loadNoMore();
        void loadMoreCompleted();
    }

    public static abstract class Presenter extends BasePresenter<FansOrFollowContract.View>{
        public Presenter(View view) {
            super(view);
        }
        abstract  void findFans(int memberId,int page);
        abstract  void findFollows(int memberId,int page);
        abstract void unFollow(int memberId,int followerId);
    }
    interface Model{
        Flowable<BaseCallModel<DataContainer<User>>> findFansByPage(int memberId,int page);
        Flowable<BaseCallModel<DataContainer<User>>> findFollowByPage(int memberId,int page);
        Flowable<BaseCallModel> unFollowUser(int memberId,int followerId);
    }
}
