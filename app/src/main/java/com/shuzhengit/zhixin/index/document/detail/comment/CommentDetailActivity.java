package com.shuzhengit.zhixin.index.document.detail.comment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.DeviceUtil;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.index.document.detail.holder.SecondaryAdapter;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
import com.shuzhengit.zhixin.view.PromptLoginDialog;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class CommentDetailActivity extends BaseActivity<CommentDetailContract.Presenter> implements
        CommentDetailContract.View {

    private static final String TAG = "CommentDetailActivity";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.recyclerView)
    PullToRefreshRecyclerView mRecyclerView;
    @BindView(R.id.etComment)
    EditText mEtComment;
    @BindView(R.id.ibSendComment)
    ImageButton mIbSendComment;
    @BindView(R.id.llRootCommentDetail)
    LinearLayout mLlRootCommentDetail;
    private int mMasterCommentId;
    private int mCurrentPage = 1;
    private int mPageSize = 10;
    private SecondaryAdapter mSecondaryAdapter;
    private AnimationSet mAnimationSet;
    private int mMasterDocumentId;
    private User mUser;
    private Unbinder mUnbinder;

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
        return R.layout.activity_comment_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
//        mTvTitle = (TextView) findViewById(R.id.tvTitle);
//         mTvRight= (TextView) findViewById(R.id.tvRight);
//         mIvRightShare= (ImageView) findViewById(R.id.ivRightShare);
//         mToolBar= (Toolbar) findViewById(R.id.toolBar);
//         mRecyclerView= (PullToRefreshRecyclerView) findViewById(R.id.recyclerView);
//         mEtComment= (EditText) findViewById(R.id.etComment);
//         mIbSendComment= (ImageButton) findViewById(R.id.ibSendComment);
//         mLlRootCommentDetail= (LinearLayout) findViewById(R.id.llRootCommentDetail);

        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mUser = CheckUser.checkUserIsExists();
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        mToolBar.setNavigationIcon(R.drawable.ic_clear_white);
        mRecyclerView.setShowPullToRefresh(false);
        mRecyclerView.setShowLoadMore(false);
        mSecondaryAdapter = new SecondaryAdapter(this);
        mRecyclerView.setAdapter(mSecondaryAdapter);
        mRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mMasterCommentId = getIntent().getIntExtra(EventCodeUtils.COMMENT_ID, 0);
        mMasterDocumentId = getIntent().getIntExtra(EventCodeUtils.DOCUMENT_ID, 0);
        mRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mBasePresenter.requestSecondaryMoreComments(mMasterCommentId, mUser == null ? 0 : mUser.getId(),
                        ++mCurrentPage,
                        mPageSize);
            }
        });
        mSecondaryAdapter.setOnItemAgreeListener(new SecondaryAdapter.OnItemAgreeListener() {
            @Override
            public void onSecondaryCommentAgree(Comment comment, TextView tvAgree, TextView tvAddOne) {
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
//                            tvAddOne.setVisibility(View.GONE);
                            Integer integer = comment.getLikeCount() + 1;
                            comment.setLikeCount(integer);
                            comment.setIsLike(1);
                            mSecondaryAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("commentId", comment.getId());
                        jsonObject.put("memberId", user.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            jsonObject.toString());
                    HttpProtocol.getApi()
                            .commentLike(requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .compose(RxResultHelper.handleResult())
                            .subscribe(new RxSubscriber<String>() {
                                @Override
                                protected void _onNext(String string) {

                                }
                            });
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(CommentDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }

            @Override
            public void onCommentAgree(Comment comment, TextView tvAgree, TextView tvAddOne) {
                LogUtils.i(TAG, comment.toString());
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
                            comment.setIsLike(1);
                            mSecondaryAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("commentId", comment.getId());
                        jsonObject.put("memberId", user.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            jsonObject.toString());
                    HttpProtocol.getApi()
                            .commentLike(requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .compose(RxResultHelper.handleResult())
                            .subscribe(new RxSubscriber<String>() {
                                @Override
                                protected void _onNext(String string) {

                                }
                            });
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(CommentDetailActivity.this);
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
        mSecondaryAdapter.setOnItemClickUserListener(new SecondaryAdapter.OnItemClickUserListener() {
            @Override
            public void onItemClickUserListener(int memberId) {
                User user = CheckUser.checkUserIsExists();
                if (user == null) {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(CommentDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(dialog -> startActivity(LoginActivity.class));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId", memberId);
                    startActivity(UserInfoActivity.class, bundle);
                }
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
        mIbSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    String s = mEtComment.getText().toString();
                    if (TextUtils.isEmpty(s)) {
                        failure("内容不能为空");
                        return;
                    }
                    mBasePresenter.releaseComment(s, user.getId(), mMasterDocumentId, mMasterCommentId);
                    mEtComment.setText("");
                    DeviceUtil.closeKeybord(CommentDetailActivity.this);
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(CommentDetailActivity.this);
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
        mBasePresenter = new CommentDetailPresenter(this);
        mBasePresenter.requestMasterComment(mMasterCommentId, mUser == null ? 0 : mUser.getId());
        mBasePresenter.requestSecondaryComments(mMasterCommentId, mUser == null ? 0 : mUser.getId(), mCurrentPage,
                mPageSize);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.normal, R.anim.bottom_out);
    }

    @Override
    public void findMasterCommentSuccess(Comment comment) {
        mTvTitle.setText("共" + comment.getCommentCount() + "条回复");
        mSecondaryAdapter.setComment(comment);
    }

    @Override
    public void findSecondaryCommentSuccess(List<Comment> comments) {
        mSecondaryAdapter.setSecondaryComments(comments);
        mRecyclerView.setShowLoadMore(true);
    }

    @Override
    public void loadMoreSuccess(List<Comment> comments) {
        mSecondaryAdapter.addMoreSecondaryComments(comments);
        mRecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadNoMore() {
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void loadMoreFail() {
        mRecyclerView.loadMoreFail();
    }

    @Override
    public void releaseCommentSuccess(Comment comment) {
        mSecondaryAdapter.addComment(comment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
