package com.shuzhengit.zhixin.column;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Column;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/21 14:01
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

 class ColumnContract {
     interface View extends BaseView<ColumnContract.Presenter>{
        void findMineFollowColumnSuccess(List<Column> mineFollowColumns);
        void findRecommendColumnSuccess(List<Column> recommendColumns);
    }
     static abstract class Presenter extends BasePresenter<ColumnContract.View>{

        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
         abstract void requestMineFollowColumn(int memberId);
         abstract void requestRecommendColumn(int memberId);
    }
     interface Model{
        Flowable<BaseCallModel<List<Column>>> fetchMineFollowColumn(int memberId);
        Flowable<BaseCallModel<List<Column>>> fetchRecommendColumn(int memberId);
    }
}
