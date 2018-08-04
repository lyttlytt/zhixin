package com.shuzhengit.zhixin.index.document;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Document;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/14 15:23
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

 class DocumentContract {
     interface View extends BaseView<DocumentContract.Presenter>{
        public void onRequestDocumentsSuccess(List<Document> columnDocument);
        public void onRefreshDocumentsSuccess(List<Document> columnDocument);
        public void onRefreshDocumentFail(String errorMessage);
        public void onLoadMoreSuccess(List<Document> columnDocument);
        public void onLoadNoMore();
        public void onLoadMoreFail(String errorMessage);
    }
     static abstract class Presenter extends BasePresenter<DocumentContract.View>{
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
//        public abstract void onRefreshNews(String columnTitle,int page);
//        public abstract void onLoadMoreNews(String columnTitle,int page);
//        public abstract void onRequestNews(String columnTitle,int page);
        public abstract void onRefreshDocuments(String columnCode,int page);
        public abstract void onLoadMoreDocuments(String columnCode,int page);
        public abstract void onRequestDocuments(String columnCode,int page);
    }
     interface Model{
        Flowable<BaseCallModel<List<Document>>> requestDocuments(String columnCode, int page);
    }
}
