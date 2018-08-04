package com.shuzhengit.zhixin.mine.collect;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Document;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 11:25
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class CollectContract {
    interface View extends BaseView<CollectContract.Presenter> {
        void refreshCollectDocuments(List<Document> documents);

        void setCollectDocuments(List<Document> documents);

        void refreshCompleted();

        void loadMoreCompleted();

        void loadNoMore();

        void loadMoreFail();
    }

    public static abstract class Presenter extends BasePresenter<CollectContract.View> {
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }

        public abstract void refreshCollectDocument(int memberId);

        public abstract void findCollectDocument(int memberId, int page, int pageNum);
    }

    interface Model {
        Flowable<BaseCallModel<List<Document>>> findDocumentByCollect(int memberId, int page, int pageSize);
    }
}
