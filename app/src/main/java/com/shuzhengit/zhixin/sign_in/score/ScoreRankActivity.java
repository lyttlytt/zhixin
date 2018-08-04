package com.shuzhengit.zhixin.sign_in.score;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 积分排行榜
 */
public class ScoreRankActivity extends BaseActivity<ScoreRankContract.Presenter> implements ScoreRankContract.View {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_score_rank;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvTitle.setText("积分排行榜");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPullToRefreshRecyclerView.setShowPullToRefresh(false);
        ScoreRankAdapter rankAdapter = new ScoreRankAdapter(this);
        mPullToRefreshRecyclerView.setAdapter(rankAdapter);
        rankAdapter.setOnItemClickListener(new ScoreRankAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                startActivity(ScoreSourceActivity.class);
            }
        });
    }

    @Override
    protected void createPresenter() {

    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void showUserRankInfo() {

    }

    @Override
    public void showRankInfos() {

    }
}
