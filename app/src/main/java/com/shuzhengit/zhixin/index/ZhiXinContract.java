package com.shuzhengit.zhixin.index;

import android.support.v4.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Column;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/14 13:28
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

class ZhiXinContract {
    interface View extends BaseView<ZhiXinContract.Presenter> {
        void findColumnSuccess(List<Column> columns, List<Fragment> fragments);
        void findColumnSuccessWithCurrentPage(List<Column> columns,List<Fragment> fragments,int columnId);
        void switchPage(int position);
    }

    static abstract class Presenter extends BasePresenter<ZhiXinContract.View> {
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }

         abstract void findColumns(int memberId);
         abstract void findColumnsWithCurrentPage(int memberId,int position);
        abstract void updateColumns(int memberId,JSONArray updateColumns);
        abstract void updateColumnsWithCurrentPage(int memberId,JSONArray updateColumns,int position);
    }

    interface Model {
        Flowable<BaseCallModel<List<Column>>> findColumns(int memberId);
        Flowable<BaseCallModel<String>> updateColumns(int memberId,JSONArray updateColumns);
        Flowable<BaseCallModel> reSubscriberColumn(int memberId);
    }
}
