package com.shuzhengit.zhixin.wenba;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.base.BaseRecyclerViewAdapter;
import com.shuzhengit.zhixin.base.BaseViewHolder;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.shuzhengit.zhixin.wenba.detail.WenBaDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoreWenBaActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.pullRecyclerView)
    PullToRefreshRecyclerView mPullRecyclerView;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_more_wen_ba;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvTitle.setText("更多问吧");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPullRecyclerView.setBackgroundColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.grey100));
        ArrayList<WenBa> followWenBas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            followWenBas.add(new WenBa());
        }
        BaseRecyclerViewAdapter<WenBa> adapter = new BaseRecyclerViewAdapter<WenBa>(R
                .layout.item_wenba_follow, followWenBas) {
            @Override
            public void convert(BaseViewHolder holder, int position, WenBa followWenBa) {

            }

            @Override
            public void onItemClick(View v, int position, WenBa followWenBa) {
                startActivity(WenBaDetailActivity.class);
            }
        };
        mPullRecyclerView.setAdapter(adapter);
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
}
