package com.shuzhengit.zhixin.wenba;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.rx.RxBus2;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.statusbar.StatusBarUtil;
import com.library.util.AppManager;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.base.BaseRecyclerViewAdapter;
import com.shuzhengit.zhixin.base.BaseViewHolder;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectCategoryActivity extends AppCompatActivity {


    private Unbinder mUnbinder;
    private Toolbar mToolBar;
    private TextView mTvTitle;
    private PullToRefreshRecyclerView mRefreshRecyclerView;
    private BaseRecyclerViewAdapter<Column> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            StatusBarUtil.setStatusBarColor(this);
        //添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        View toolBar = LayoutInflater.from(this).inflate(R.layout.toolbar, linearLayout, false);
        linearLayout.addView(toolBar);
        View recyclerView = LayoutInflater.from(this).inflate(R.layout.fragment_wenba_recyclerview, linearLayout,
                false);
        linearLayout.addView(recyclerView);
        setContentView(linearLayout);
        mUnbinder = ButterKnife.bind(this);
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mRefreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.pullRecyclerView);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTvTitle.setText("选择分类");
        mRefreshRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mAdapter = new
                BaseRecyclerViewAdapter<Column>(R.layout.item_category) {
            @Override
            public void convert(BaseViewHolder holder, int position, Column column) {
                holder.setText(R.id.tvCategory,column.getColumnTitle());
            }

            @Override
            public void onItemClick(View v, int position, Column column) {
                RxBus2.getDefault().post(new EventType<Column>(EventCodeUtils.WEN_BA_CATEGORY,column));
                onBackPressed();
            }
        };
        mRefreshRecyclerView.setAdapter(mAdapter);
        mRefreshRecyclerView.setShowLoadMore(false);
        mRefreshRecyclerView.setShowPullToRefresh(false);
        findCategory();
    }

    private void findCategory() {
        HttpProtocol.getApi()
                .findCategory()
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Column>>() {
                    @Override
                    protected void _onNext(List<Column> columns) {
                        mAdapter.setData(columns);
                    }
                });
//                .subscribeWith(new RxSubscriber<List<Column>>>() {
//                    @Override
//                    protected void _onNext(List<Column> columns) {
//                        mAdapter.setData(questionCategoryDataContainer.getList());
//                    }
//                });
    }


    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        //结束activity从堆栈中移除
        AppManager.getAppManager().removeActivity(this);
        super.onDestroy();
    }
}
