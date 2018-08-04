package com.shuzhengit.zhixin.wenba.detail.reply;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AskComment;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/14 17:41
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ReplyContract {
    interface View extends BaseView<ReplyContract.Presenter>{
        void setAskWithReplyInfo(AskWithReply askWithReplyInfo);
        void commentSuccess();
        void refreshComments(DataContainer<AskComment> dataContainer);
        void addMoreComments(DataContainer<AskComment> dataContainer);
        void noMore();
        void loadMoreFail();
        void loadMoreComplete();
    }
    public static abstract class Presenter extends BasePresenter<ReplyContract.View>{

        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }
        abstract void comment(String content,int askId,int wenBaId,int replayUserId);
        abstract void findAskById(int askId,int userId);
        abstract void agreeAsk(int askId,int userId);
        abstract void findCommentById(int page,int pageSize,int userId,int askId);
        abstract void refreshComments(int page,int pageSize,int userId,int askId);
        abstract void agreeComment(int askId,int commentId,int userId);
    }
    interface Model {
        Flowable<BaseCallModel> postComment(String content,int askId,int wenBaId,int replayUserId);
        Flowable<BaseCallModel<AskWithReply>> findAskWithReplyById(int askId,int userId);
        Flowable<BaseCallModel> agreeAsk(int askId,int userId);

        Flowable<BaseCallModel<DataContainer<AskComment>>> fetchComments(int page,int pageSize,int userId,int askId);
        Flowable<BaseCallModel> agreeComment(int askId,int commentId,int userId);
    }
}
