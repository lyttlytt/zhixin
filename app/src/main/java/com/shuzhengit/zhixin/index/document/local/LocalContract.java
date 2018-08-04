package com.shuzhengit.zhixin.index.document.local;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Document;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/18 09:24
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class LocalContract {
    interface View extends BaseView<LocalContract.Presenter>{

        public void onRequestLocalSchoolSuccess(List<Document> schoolDocument);
        public void onRefreshLocalSchoolSuccess(List<Document> schoolDocument);

        public void onRequestLocalCitySuccess(List<Document> cityDocuments);
        public void onRefreshLocalCitySuccess(List<Document> cityDocuments);

        public void onLoadMoreLocalSchool(List<Document> schoolDocument);
        public void onLoadMoreLocalCity(List<Document> cityDocument);

        public void onRefreshFail(String errorMessage);
        public void onLoadNoMore();
        public void onLoadMoreFail(String errorMessage);
        public void onLoadMoreComplete();
        public void onRefreshComplete();
    }
    public static abstract class Presenter extends BasePresenter<LocalContract.View>{
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }

        public abstract void onRefreshLocalSchool(int schoolId,int page);
        public abstract void onLoadMoreLocalSchool(int schoolId,int page);
        public abstract void onRequestLocalSchool(int schoolId,int page);

        abstract void onRefreshLocalCity(String cityCode);
        abstract void onLoadMoreLocalCity(String cityCode,int page);
        abstract void onRequestLocalCity(String cityCode,int page);

    }
    interface Model{

        Flowable<BaseCallModel<List<Document>>> fetchLocalSchool(int schoolId,int page);

        Flowable<BaseCallModel<List<Document>>> fetchLocalCity(String cityCode,int page);
    }
}
