package com.shuzhengit.zhixin.index.document.detail;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.permission.AcpListener;
import com.library.permission.AcpOptions;
import com.library.permission.AcpUtils;
import com.library.util.DeviceUtil;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.DocumentPicture;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.index.document.PhotoViewActivity;
import com.shuzhengit.zhixin.index.document.detail.comment.CommentDetailActivity;
import com.shuzhengit.zhixin.index.document.detail.holder.DocumentDetailAdapter;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.KeyBorderLeakUtil;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.util.SocializeMediaTypeUtils;
import com.shuzhengit.zhixin.util.SocializeShareUtils;
import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
import com.shuzhengit.zhixin.view.PromptLoginDialog;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DocumentDetailActivity extends BaseActivity<DocumentDetailContract.Presenter> implements
        DocumentDetailContract.View {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.pullRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R.id.fabCollect)
    FloatingActionButton mFabCollect;
    @BindView(R.id.etComment)
    EditText mEtComment;
    @BindView(R.id.ibSendComment)
    ImageButton mIbSendComment;
    private DocumentDetailAdapter mDocumentDetailAdapter;
    private static final String TAG = "DocumentDetailActivity";
    private PromptLoginDialog mPromptLoginDialog;
    private int mDocumentId;
    private int mCurrentPage = 1;
    private AnimationSet mAnimationSet;
    private User mUser;
    private Unbinder mUnbinder;
    private String mEsId;

    @Override
    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart(TAG);
        //统计时长
        MobclickAgent.onResume(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        //统计页面
        MobclickAgent.onPageEnd(TAG);
        //统计页面的时长
        MobclickAgent.onPause(this);

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_document_detail;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mPullToRefreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.pullRecyclerView);
        mTvRight = (TextView) findViewById(R.id.tvRight);
        mIvRightShare = (ImageView) findViewById(R.id.ivRightShare);
        mEtComment = (EditText) findViewById(R.id.etComment);
        mIbSendComment = (ImageButton) findViewById(R.id.ibSendComment);
        mFabCollect = (FloatingActionButton) findViewById(R.id.fabCollect);

        mDocumentId = getIntent().getIntExtra(EventCodeUtils.DOCUMENT_ID, -1);
        mEsId = getIntent().getStringExtra("esId");

        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        mPullToRefreshRecyclerView.setShowPullToRefresh(false);
        mPullToRefreshRecyclerView.setShowLoadMore(false);

        mUser = CheckUser.checkUserIsExists();
        mIvRightShare.setVisibility(View.VISIBLE);
        mDocumentDetailAdapter = new DocumentDetailAdapter(this);
        mPullToRefreshRecyclerView.setAdapter(mDocumentDetailAdapter);
        mPullToRefreshRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mBasePresenter.loadMoreComment(mDocumentId, mUser == null ? 0 : mUser.getId(), ++mCurrentPage, 10);
            }
        });

        mIbSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = CheckUser.checkUserIsExists();
                if (user == null) {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(DocumentDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                    return;
                }
                String comment = mEtComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    failure("评论不能为空!");
                    return;
                }
                //一级评论
                mBasePresenter.releaseDocumentComment(comment, user.getId(), mDocumentId);
                mEtComment.setText("");
                DeviceUtil.closeKeybord(DocumentDetailActivity.this);
            }
        });
        mDocumentDetailAdapter.setOnCommentItemClickListener(new DocumentDetailAdapter.OnCommentItemClickListener() {
            @Override
            public void onCommentItemClickListener(Comment comment) {
                //根据点击的item评论获取二级评论列表
                Bundle bundle = new Bundle();
                bundle.putInt(EventCodeUtils.COMMENT_ID, comment.getId());
                bundle.putInt(EventCodeUtils.DOCUMENT_ID, mDocumentId);
                startActivity(CommentDetailActivity.class, bundle);
                overridePendingTransition(R.anim.top_in, R.anim.normal);
            }
        });
        mDocumentDetailAdapter.setOnContentItemImage(new DocumentDetailAdapter.OnContentItemImage() {
            @Override
            public void onContentItemImage(DocumentPicture url) {
                Bundle bundle = new Bundle();
                bundle.putString(EventCodeUtils.DOCUMENT_PICTURE,url.getSrc());
                startActivity(PhotoViewActivity.class, bundle);
            }
        });
        mDocumentDetailAdapter.setOnItemClickUserListener(memberId -> {
            User user = CheckUser.checkUserIsExists();
            if (user == null) {
                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(DocumentDetailActivity.this);
                promptLoginDialog.show();
                promptLoginDialog.setDialogSingleCallBack(dialog -> startActivity(LoginActivity.class));
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("memberId", memberId);
                startActivity(UserInfoActivity.class, bundle);
            }
        });
        mDocumentDetailAdapter.setOnDocumentAgree(new DocumentDetailAdapter.OnDocumentAgree() {
            @Override
            public void onDocumentAgree(Document document, TextView tvAddOne, TextView tvLikeCount, ImageView
                    ivLikeIcon, RelativeLayout llLike) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    tvAddOne.setVisibility(View.VISIBLE);
                    tvAddOne.startAnimation(mAnimationSet);
                    mBasePresenter.agreeDocument(document.getId(), user.getId());
                    mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tvAddOne.setVisibility(View.GONE);
                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            int color = ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary);
                            drawable.setStroke(2, ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
                            int i = document.getLikeCount() + 1;
                            document.setLikeCount(i);
                            document.setIsLike(1);
                            tvLikeCount.setText(String.valueOf(document.getLikeCount()));
                            llLike.setBackground(drawable);
                            ivLikeIcon.setImageResource(R.drawable.ic_document_like);
                            tvLikeCount.setTextColor(color);
                            llLike.setClickable(false);
//                            mDocumentDetailAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(DocumentDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });
        mDocumentDetailAdapter.setOnItemCommentAgree(new DocumentDetailAdapter.OnItemCommentAgree() {
            @Override
            public void onItemCommentAgree(Comment comment, TextView tvAddOne, TextView tvAgree) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    tvAddOne.setVisibility(View.VISIBLE);
                    tvAddOne.startAnimation(mAnimationSet);
                    mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tvAddOne.setVisibility(View.GONE);
                            Integer integer = comment.getLikeCount() + 1;
                            comment.setLikeCount(integer);
                            tvAgree.setText(String.valueOf(comment.getLikeCount()));

                            Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable
                                    .ic_like);
                            tvAgree.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
                            likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
                            tvAgree.setCompoundDrawables(likeIcon, null, null, null);
                            comment.setIsLike(1);
                            tvAgree.setClickable(false);
//                            mDocumentDetailAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mBasePresenter.agreeComment(comment.getId(), user.getId());
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(DocumentDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });
        mDocumentDetailAdapter.setOnRecommendItemClickListener(position -> failure("跳转到推荐的文章"));
        mFabCollect.setOnClickListener(v -> {
            User user = CheckUser.checkUserIsExists();
            if (user == null) {
                mPromptLoginDialog = new PromptLoginDialog(this);
                mPromptLoginDialog.show();
                mPromptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                    @Override
                    public void callBackPositive(DialogInterface dialog) {
                        startActivity(LoginActivity.class);
                    }
                });
            } else {
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
//            new RotateAnimation()
                scaleAnimation.setDuration(300);
                scaleAnimation.setFillAfter(false);
                mFabCollect.startAnimation(scaleAnimation);
                mBasePresenter.collectOrCancelDocument(mDocumentId, user.getId());
            }

        });
        mEtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mIbSendComment.setImageResource(R.drawable.ic_no_send);
                } else {
                    mIbSendComment.setImageResource(R.drawable.ic_send_comment);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    @Override
    protected void createPresenter() {
        mAnimationSet = new AnimationSet(this, null);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        alphaAnimation.setDuration(1000);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -50);
        translateAnimation.setDuration(1000);
        mAnimationSet.addAnimation(translateAnimation);
        mAnimationSet.addAnimation(alphaAnimation);
        mBasePresenter = new DocumentDetailPresenter(this);
        User user = CheckUser.checkUserIsExists();
        mPullToRefreshRecyclerView.getItemAnimator().setChangeDuration(0);
        ((SimpleItemAnimator)mPullToRefreshRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        if (TextUtils.isEmpty(mEsId)){
            failure("文章不存在,请稍后重试");
            onBackPressed();
        }
        if (user != null) {
            mBasePresenter.requestDocumentDetail(mEsId, user.getId());

//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("documentId", mDocumentId);
//                jsonObject.put("memberId", user.getId());
////                jsonObject.put("latestFlag", 1);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
//                    jsonObject.toString());
//            HttpProtocol.getApi()
//                    .readHistory(requestBody)
//                    .compose(RxSchedulersHelper.io_main())
//                    .subscribe(new RxSubscriber<BaseCallModel>() {
//                        @Override
//                        protected void _onNext(BaseCallModel callModel) {
//
//                        }
//                    });
        } else {
            mBasePresenter.requestDocumentDetail(mEsId, 0);
        }
        mBasePresenter.requestDocumentComments(mDocumentId, mUser == null ? 0 : mUser.getId(), mCurrentPage, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //完成回调
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        UMShareAPI.get(this).release();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            KeyBorderLeakUtil.fixFocusedViewLeak(APP.getInstance());
        }
    }


    @Override
    public void isAgreeDocument(boolean isLike) {

    }

    @Override
    public void requestRecommendDocumentsSuccess(List<Document> recommendDocuments) {
        mDocumentDetailAdapter.setRecommendDocument(recommendDocuments);
    }

    @Override
    public void requestDocumentCommentsSuccess(List<Comment> comments) {
        mDocumentDetailAdapter.addMore(comments);
        mPullToRefreshRecyclerView.setShowLoadMore(true);
    }

    @Override
    public void releaseDocumentCommentSuccess(Comment comment) {
        mDocumentDetailAdapter.addComment(comment);
    }

    @Override
    public void isCollectDocument(boolean isFavourite) {
        if (isFavourite) {
            mFabCollect.setImageResource(R.drawable.ic_favorite_collect);
        } else {
            mFabCollect.setImageResource(R.drawable.ic_favorite_uncollect);
        }
    }

    @Override
    public void collectOrCancelDocument(boolean status) {
        if (status) {
            failure("收藏成功");
        } else {
            failure("取消收藏");
        }
        isCollectDocument(status);
    }


    @Override
    public void findDocumentDetailSuccess(Document document) {
        LogUtils.i(TAG, document.toString());
        mDocumentDetailAdapter.setContent(document);
        mToolBar.setTitle(document.getTitle());
        mToolBar.setTitleTextColor(Color.WHITE);


        mIvRightShare.setOnClickListener(v -> {
            AcpUtils.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission
                    .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
                @Override
                public void onGranted() {
                    List<DocumentPicture> allPic = document.getAllPic();
                    String content = document.getContent();
                    if (allPic.size()!=0){
                         content = changeContent(content,allPic);
                    }
                    new SocializeShareUtils.Builder()
                            .setSocializeMedia(SocializeMediaTypeUtils.QQ, SocializeMediaTypeUtils.WEIXIN,
                                    SocializeMediaTypeUtils.WEIXIN_CIRCLE, SocializeMediaTypeUtils.SINA)
                            .setUMWebTitle(document.getTitle())
                            .setUMWebUrl(APP.SHARE_URL)
                            .setUMWebDescription(content)
                            .setUMWebThumb(R.mipmap.logo)
                            .buildUMWeb(DocumentDetailActivity.this)
                            .openShare(DocumentDetailActivity.this, new SocializeShareUtils.ShareListener());
                }

                @Override
                public void onDenied(List<String> permissions) {

                }
            });

        });

    }

    private String changeContent(String content, List<DocumentPicture> allPic) {
        String oldChars = "";
        String newChars = "";
        for (int i = 0; i < allPic.size(); i++) {
            oldChars = "<!--IMG#" + i + "-->";
            // 在客户端解决WebView图片屏幕适配的问题，在<img标签下添加style='max-width:90%;height:auto;'即可
            // 如："<img" + " style=max-width:100%;height:auto; " + "src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>"
//            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>";
//            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>"+"<p></p>";
            content = content.replace(oldChars, newChars);
        }
        LogUtils.e("TAG",content);
        return content;
    }

    @Override
    public void loadMoreSuccess(List<Comment> comments) {
        mDocumentDetailAdapter.addMore(comments);
        mPullToRefreshRecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void loadNoMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }


}
