package com.shuzhengit.zhixin.sign_in.score;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2018/1/29 11:34
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ScoreRankContract {
    interface View extends BaseView<ScoreRankContract.Presenter>{
        void showUserRankInfo();
        void showRankInfos();
    }
    static abstract class Presenter extends BasePresenter<ScoreRankContract.View> {
        public Presenter(View view) {
            super(view);
            view.setPresenter(this);
        }
        abstract void requestUserRankInfo(int userId);
        abstract void requestRankInfos(int page,int pageSize);
    }
    interface Model{
        Flowable<BaseCallModel> fetchUserRankInfo(int userId);
        Flowable<BaseCallModel> fetchRankInofs(int page ,int pageSize);

    }
}
