package com.shuzhengit.zhixin.index.document.history;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Document;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/21 16:13
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

class HistoryOrCollectContract {
    interface View extends BaseView<HistoryOrCollectContract.Presenter> {
        void findDocumentByHistorySuccess(List<Document> documentPageContainer);

        void findDocumentByCollectSuccess(List<Document> documentPageContainer);

        void refreshSuccess(List<Document> documentPageContainer);

        void refreshFail();

        void loadMoreSuccess(List<Document> documentPageContainer);

        void loadNoMore();

        void loadFail();
    }

    static abstract class Presenter extends BasePresenter<HistoryOrCollectContract.View> {
        Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }

        abstract void requestDocumentByHistory(int memberId, int page, int pageSize);

        abstract void requestDocumentByCollect(int memberId, int page, int pageSize);

        abstract void requestMoreDocument(int memberId,int page,int pageSize,boolean isHistory);

        abstract void refreshDocument(int memberId,int page,int pageSize,boolean isHistory);
    }

    interface Model {
        Flowable<BaseCallModel<List<Document>>> findDocumentByHistory(int memberId, int page, int pageSize);

        Flowable<BaseCallModel<List<Document>>> findDocumentByCollect(int memberId, int page, int pageSize);
    }
}
