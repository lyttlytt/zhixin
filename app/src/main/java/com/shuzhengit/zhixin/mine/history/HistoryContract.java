package com.shuzhengit.zhixin.mine.history;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.WenBa;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 10:33
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class HistoryContract {
    interface View extends BaseView<Presenter> {
        void setDocuments(List<Document> documents);
        void refreshDocument(List<Document> documents);
        void setQuestions(DataContainer<WenBa> dataContainer);
        void refreshQuestions(DataContainer<WenBa> dataContainer);
        void refreshCompleted();
        void loadMoreCompleted();
        void loadMoreFail();
        void loadNoMore();
    }

    static abstract class Presenter extends BasePresenter<View> {
        Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }

        abstract void requestDocumentByHistory(int memberId, int page, int pageSize);

        abstract void refreshDocumentByHistory(int memberId);


        abstract void requestQuestionByHistory(int memberId, int page, int pageSize);

        abstract void refreshQuestionByHistory(int memberId);
    }

    interface Model {
        Flowable<BaseCallModel<List<Document>>> findDocumentByHistory(int memberId, int page, int pageSize);

        Flowable<BaseCallModel<DataContainer<WenBa>>> findQuestionByHistory(int memberId, int page, int pageSize);
    }
}
