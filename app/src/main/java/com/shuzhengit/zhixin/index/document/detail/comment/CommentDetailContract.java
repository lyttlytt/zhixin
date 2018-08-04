package com.shuzhengit.zhixin.index.document.detail.comment;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Comment;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/7 09:04
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

 class CommentDetailContract {
    interface View extends BaseView<CommentDetailContract.Presenter>{
        void findMasterCommentSuccess(Comment comment);
        void findSecondaryCommentSuccess(List<Comment> comments);
        void loadMoreSuccess(List<Comment> comments);
        void loadNoMore();
        void loadMoreFail();
        void releaseCommentSuccess(Comment comment);
    }
    abstract static class Presenter extends BasePresenter<CommentDetailContract.View>{
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
        abstract void requestMasterComment(int masterCommentId, int memberId);
        abstract void requestSecondaryComments(int masterId,int memberId,int page,int pageSize);
        abstract void requestSecondaryMoreComments(int masterId,int memberId,int page,int pageSize);
        abstract void releaseComment(String commentContent,int userId,int documentId,int masterCommentId);
    }
    interface Model{
        Flowable<BaseCallModel<Comment>> fetchMasterComment(int id,int memberId);
        Flowable<BaseCallModel<List<Comment>>> fetchSecondaryComments(int masterId,int memberId,int page,int pageSize);
        Flowable<BaseCallModel<Comment>> releaseComment(String commentContent,int userId,int documentId,int
                                                        masterCommentId);
    }
}
