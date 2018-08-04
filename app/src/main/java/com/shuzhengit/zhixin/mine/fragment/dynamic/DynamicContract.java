package com.shuzhengit.zhixin.mine.fragment.dynamic;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Dynamic;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 10:50
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DynamicContract {
    interface View extends BaseView<DynamicContract.Presenter>{
        void setDynamics(List<Dynamic> dynamics);
        void loadMoreCompleted();
        void loadMoreFail(String message);
        void loadMoreNoData();
    }
    public static abstract class Presenter extends BasePresenter<DynamicContract.View>{
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
        public abstract void findDynamics(int memberId,int page,int pageNum);
    }
    interface Model{
        Flowable<BaseCallModel<List<Dynamic>>> fetchDynamics(int memberId,int page,int pageNum);
    }
}
