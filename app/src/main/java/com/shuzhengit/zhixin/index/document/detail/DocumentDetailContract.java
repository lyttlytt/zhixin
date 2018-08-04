package com.shuzhengit.zhixin.index.document.detail;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.bean.Document;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/16 16:05
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

class DocumentDetailContract {
    interface View extends BaseView<DocumentDetailContract.Presenter> {
        //是否点赞过这篇文章的回调
        public void isAgreeDocument(boolean isLike);

        //请求推荐的文章回调
        public void requestRecommendDocumentsSuccess(List<Document> recommendDocuments);

        //请求文章的一级评论回调
        public void requestDocumentCommentsSuccess(List<Comment> comments);

        public void releaseDocumentCommentSuccess(Comment comment);

        //请求收藏状态回调
        public void isCollectDocument(boolean isFavourite);

        //给一级评论点赞回调
        // public void requestCommentAgreeSuccess();

        //收藏/取消一篇文章
        public void collectOrCancelDocument(boolean status);

        //二级评论
//        public void requestSecondaryComment(List<Comment> secondaryComments);

        //发表二级评论的回调
//        public void releaseSecondaryCommentSuccess(Comment comment);

        //获取文章详情
        public void findDocumentDetailSuccess(Document document);

        //一级评论加载更多
        public void loadMoreSuccess(List<Comment> comments);

        //加载更多失败
        public void loadMoreFail();

        //没有更多
        public void loadNoMore();
    }

    static abstract class Presenter extends BasePresenter<DocumentDetailContract.View> {
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }

        //获取文章详情
//        public abstract void requestDocumentDetail(int documentId, int userId);
        public abstract void requestDocumentDetail(String esId, int userId);
        //是否给这边文章点赞过
//        public abstract void checkIsAgreeDocument(int documentId, int userId);

        //收藏状态
//        public abstract void checkCollectDocument(int documentId, int userId);

        //请求推荐的文章
        public abstract void requestRecommendDocument(int documentId);

        //请求文章一级评论
        public abstract void requestDocumentComments(int documentId, int memberId, int page, int pageSize);

        //给一级评论点赞
        public abstract void agreeComment(int commentId, int userId);

        //收藏/取消一篇文章
        public abstract void collectOrCancelDocument(int documentId, int userId);

        //给文章点赞
        public abstract void agreeDocument(int documentId, int userId);

        //分页获取二级评论
//        public abstract void secondaryComments(int commentId,int memberId, int page);

        //发表一级评论
        public abstract void releaseDocumentComment(String commentContent, int userId, int documentId);

        //发表二级评论
//        public abstract void releaseSecondaryComment(String commentContent, int userId, int documentId, int
//                upperCommentId);

        //加载更多
        public abstract void loadMoreComment(int documentId, int memberId, int page, int pageSize);
    }

    public interface Model {
        //分页获取一级评论
        Flowable<BaseCallModel<List<Comment>>> fetchComments(int documentId, int memberId, int page, int pageSize);

        //检查是否点赞过这篇文章
//        Flowable<BaseCallModel> checkIsAgreeDocument(int documentId, int userId);

        //检查是否收藏过
//        Flowable<BaseCallModel> checkIsCollectDocument(int documentId, int userId);

        //给评论点赞
        Flowable<BaseCallModel<String>> agreeComment(int commentId, int userId);

        //获取推荐的文章
        Flowable<BaseCallModel<List<Document>>> fetchRecommendDocument(int documentId);

        /**
         * 发表片一个评论
         *
         * @param commentContent 评论内容
         * @param userId         用户的id
         * @param documentId     文章的id 如果是二级评论,就是一级评论的id
         * @param upperCommentId 给文章评论的话就是0,否则是上级评论的id
         * @return
         */
        Flowable<BaseCallModel<Comment>> releaseComment(String commentContent, int userId, int documentId, int
                upperCommentId);

        //是否收藏这篇文章
        Flowable<BaseCallModel> collectOrCancelDocument(int documentId, int userId);

        //给文章点赞
        Flowable<BaseCallModel> agreeDocument(int document, int userId);

        //获取二级评论
//        Flowable<BaseCallModel<List<Comment>>> fetchSecondaryComments(int commentId,int memberId, int page, int
//                pageSize);
        //获取文章详情
//        Flowable<BaseCallModel<Document>> fetchDocumentDetail(int documentId, int memberId);
        Flowable<BaseCallModel<Document>> fetchDocumentDetail(String esId, int memberId);
    }
}
