//package com.shuzhengit.zhixin.view;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.StyleRes;
//import android.support.v7.widget.DividerItemDecoration;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.TranslateAnimation;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//import com.library.util.ToastUtils;
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.index.document.detail.holder.SecondaryAdapter;
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/8/16 09:47
// * E-mail:yuancongbin@gmail.com
// * <p>
// * 功能描述:
// */
//
//public class CommentDetailDialog extends Dialog {
//
//    private AnimationSet mOutAnimation;
//    private AnimationSet mInAnimation;
//    private View mDialogView;
//    private PullToRefreshRecyclerView mSecondaryRecyclerView;
//    private EditText mEtComment;
//    private ImageButton mIbSendComment;
//
//    public CommentDetailDialog(@NonNull Context context) {
//        this(context,0);
//    }
//
//    public CommentDetailDialog(@NonNull Context context, @StyleRes int themeResId) {
//        super(context, R.style.baseDialog);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        startWithAnimation(true);
//    }
//
//    private void startWithAnimation(boolean isShowInAnimation) {
//        if (isShowInAnimation){
//            mDialogView.startAnimation(mInAnimation);
//        }
//    }
//
//    @Override
//    public void dismiss() {
//        dismissWithOutAnimation(true);
//    }
//
//    private void dismissWithOutAnimation(boolean isShowOutAnimation) {
//        if (isShowOutAnimation)
//            mDialogView.startAnimation(mOutAnimation);
//        else
//            super.dismiss();
//    }
//    public interface onReleaseSecondaryCommentListener{
//        void onReleaseSecondaryComment(String commentContent,int userId,int documentId,int upperCommentId);
//        void onCommentAgree(int commentId,int userId);
//        void onSecondaryAgree(int commentId,int userId);
//    }
//    public onReleaseSecondaryCommentListener mOnReleaseSecondaryCommentListener=null;
//
//    public void setOnReleaseSecondaryCommentListener(onReleaseSecondaryCommentListener
//                                                           onReleaseSecondaryCommentListener) {
//        mOnReleaseSecondaryCommentListener = onReleaseSecondaryCommentListener;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mInAnimation = getInAnimation(getContext());
//        mOutAnimation = getOutAnimation(getContext());
//        initAnimationListener();
//        setContentView(R.layout.activity_comment_detail);
//
//        Window window = getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        window.getDecorView().setPadding(0,0,0,0);
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.height=WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(layoutParams);
//        mDialogView = findViewById(R.id.llRootCommentDetail);
//        mSecondaryRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id
//                .commentRecyclerView);
//        mSecondaryRecyclerView.setShowPullToRefresh(false);
//        mSecondaryRecyclerView.setShowLoadMore(true);
//        mSecondaryRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
//        SecondaryAdapter secondaryAdapter = new SecondaryAdapter(getContext());
//        secondaryAdapter.setOnItemAgreeListener(new SecondaryAdapter.OnItemAgreeListener() {
//            @Override
//            public void onCommentAgree(int commentId, int userId) {
//                if (mOnReleaseSecondaryCommentListener!=null){
//                    mOnReleaseSecondaryCommentListener.onCommentAgree(commentId, userId);
//                }
//            }
//
//            @Override
//            public void onSecondaryCommentAgree(int commentId, int userId) {
//                if (mOnReleaseSecondaryCommentListener!=null){
//                    mOnReleaseSecondaryCommentListener.onSecondaryAgree(commentId, userId);
//                }
//            }
//        });
//        mSecondaryRecyclerView.setAdapter(secondaryAdapter);
//        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        mEtComment = (EditText) findViewById(R.id.etComment);
//
//        mIbSendComment = (ImageButton) findViewById(R.id.ibSendComment);
//        mIbSendComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String s = mEtComment.getText().toString();
//                if (TextUtils.isEmpty(s)) {
//                    ToastUtils.showShortToast(getContext(),"评论不能为空");
//                    return;
//                }
//                if (mOnReleaseSecondaryCommentListener!=null){
//                    // TODO: 2017/8/18 需要用户信息
////                    mOnReleaseSecondaryCommentListener.onReleaseSecondaryComment(s,);
//                }
//            }
//        });
//        mEtComment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (s.length()>0){
//                        mIbSendComment.setImageResource(R.drawable.ic_send_comment);
//                    }else {
//                        mIbSendComment.setImageResource(R.drawable.ic_no_send);
//                    }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    private void initAnimationListener() {
//        mOutAnimation.setAnimationListener(
//                new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mDialogView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        CommentDetailDialog.super.dismiss();
//                    }
//                });
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//    }
//
//    private AnimationSet getOutAnimation(Context context) {
//        AnimationSet animationSet = new AnimationSet(context, null);
//        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation
//                .RELATIVE_TO_SELF, 0.0f, Animation
//                .RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
//        translateAnimation.setDuration(300);
//        translateAnimation.setFillAfter(true);
//        animationSet.addAnimation(translateAnimation);
//        animationSet.setFillAfter(true);
//        return animationSet;
//    }
//
//    private AnimationSet getInAnimation(Context context) {
//        AnimationSet animationSet = new AnimationSet(context, null);
//        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation
//                .RELATIVE_TO_SELF, 0.0f, Animation
//                .RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        translateAnimation.setDuration(300);
//        translateAnimation.setFillAfter(true);
//        animationSet.addAnimation(translateAnimation);
//        animationSet.setFillAfter(true);
//        return animationSet;
//    }
//
//}
