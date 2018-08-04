package com.shuzhengit.zhixin.column;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.library.base.BaseActivity;
import com.library.rx.RxBus2;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.column.holder.ColumnAdapter;
import com.shuzhengit.zhixin.column.holder.ColumnAdapter2;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CacheUtils;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.ColumnDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ColumnActivity extends BaseActivity<ColumnContract.Presenter>
        implements ColumnContract.View {
    private static final String TAG = "ColumnActivity";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.columnRecyclerView)
    RecyclerView mColumnRecyclerView;
    private Unbinder mUnbinder;
    private ColumnAdapter2 mColumnAdapter;
    private int mCurrentColumnPosition = -1;

    @Override
    protected int layoutId() {
        return R.layout.activity_column;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvRight = (TextView) findViewById(R.id.tvRight);
        mIvRightShare = (ImageView) findViewById(R.id.ivRightShare);
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mColumnRecyclerView = (RecyclerView) findViewById(R.id.columnRecyclerView);
        setSupportActionBar(mToolBar);
        mCurrentColumnPosition = getIntent().getIntExtra("currentColumnPosition", -1);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(v -> {
            boolean modify = mColumnAdapter.isModify();
            LogUtils.d(TAG, "onback setNavigationOnClickListener : " + modify);
            if (modify) {
                boolean switchCurrentPage = false;
                List<Column> myChannelItems = mColumnAdapter.getMyChannelItems();
                for (Column myChannelItem : myChannelItems) {
                    if (mCurrentColumnPosition == myChannelItem.getId()) {
                        switchCurrentPage = true;
                        break;
                    }
                }
                if (!switchCurrentPage) {
                    String json = JSON.toJSONString(mColumnAdapter.getMyChannelItems());
                    JSONArray jsonArray = JSON.parseArray(json);
                    RxBus2.getDefault().post(new EventType<JSONArray>(EventCodeUtils.MODIFY_COLUMN, jsonArray));
                    LogUtils.d(TAG, "onback setNavigationOnClickListener : " + modify + "post : MODIFY_COLUMN "+"23333333333333333333");
                }else {
                    String json = JSON.toJSONString(mColumnAdapter.getMyChannelItems());
                    JSONArray jsonArray = JSON.parseArray(json);
                    RxBus2.getDefault().post(new EventType<JSONArray>(EventCodeUtils.ISMODIFY_CLICK_COLUMN,
                            jsonArray,mCurrentColumnPosition));
                    LogUtils.d(TAG, "onback setNavigationOnClickListener : " + modify + "post : MODIFY_COLUMN ");
                }
//                String json = JSON.toJSONString(mColumnAdapter.getMyChannelItems());
//                JSONArray jsonArray = JSON.parseArray(json);
//                RxBus2.getDefault().post(new EventType<JSONArray>(EventCodeUtils.MODIFY_COLUMN, jsonArray));
            }
            onBackPressed();
        });
        mTvTitle.setText("频道管理");
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
//        mColumnAdapter = new ColumnAdapter(this);
        mColumnAdapter = new ColumnAdapter2(this, mColumnRecyclerView, 1, 1, mCurrentColumnPosition);
        mColumnRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isHeader = mColumnAdapter.getItemViewType(position) == ColumnAdapter
                        .ITEM_COLUMN_HEADER || mColumnAdapter.getItemViewType(position) == ColumnAdapter
                        .ITEM_RECOMMEND_COLUMN_HEADER;
                return isHeader ? 4 : 1;
            }
        });
        mColumnRecyclerView.addItemDecoration(new ColumnDecoration(ColumnAdapter.ITEM_SPACE));
        mColumnRecyclerView.setAdapter(mColumnAdapter);
        mColumnAdapter.setChannelItemClickListener(new ColumnAdapter2.ChannelItemClickListener() {
            @Override
            public void onChannelItemClick(List<Column> list, int position) {
                if (!mColumnAdapter.isModify()) {
                    LogUtils.d(TAG, list.get(position).getColumnTitle());
                    EventType<Column> columnEventType = new EventType<Column>(EventCodeUtils.CLICK_COLUMN, list.get
                            (position));
                    RxBus2.getDefault().post(columnEventType);
                    LogUtils.d(TAG, columnEventType.toString());
                    onBackPressed();
                } else {
                    String json = JSON.toJSONString(mColumnAdapter.getMyChannelItems());
                    JSONArray jsonArray = JSON.parseArray(json);
                    RxBus2.getDefault().post(new EventType<JSONArray>(EventCodeUtils.ISMODIFY_CLICK_COLUMN,
                            jsonArray, list.get(position).getId()));
                    onBackPressed();
                }
            }
        });
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new ColumnPresenter(this);
        User user = (User) CacheUtils.getCacheManager(this).getAsObject(CacheKeyManager.USER);
        mBasePresenter.requestMineFollowColumn(user.getId());
        mBasePresenter.requestRecommendColumn(user.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void findMineFollowColumnSuccess(List<Column> mineFollowColumns) {
        mColumnAdapter.setMyChannelItems(mineFollowColumns);
    }

    @Override
    public void findRecommendColumnSuccess(List<Column> recommendColumns) {
        mColumnAdapter.setOtherChannelItems(recommendColumns);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean modify = mColumnAdapter.isModify();
            LogUtils.d(TAG, "onback onKeyDown : " + modify);
            if (modify) {
                boolean switchCurrentPage = false;
                List<Column> myChannelItems = mColumnAdapter.getMyChannelItems();
                for (Column myChannelItem : myChannelItems) {
                    if (mCurrentColumnPosition == myChannelItem.getId()) {
                        switchCurrentPage = true;
                        break;
                    }
                }
                if (!switchCurrentPage) {
                    String json = JSON.toJSONString(mColumnAdapter.getMyChannelItems());
                    JSONArray jsonArray = JSON.parseArray(json);
                    RxBus2.getDefault().post(new EventType<JSONArray>(EventCodeUtils.MODIFY_COLUMN, jsonArray));
                    LogUtils.d(TAG, "onback onKeyDown : " + modify + "post : MODIFY_COLUMN ");
                }else {
                    String json = JSON.toJSONString(mColumnAdapter.getMyChannelItems());
                    JSONArray jsonArray = JSON.parseArray(json);
                    RxBus2.getDefault().post(new EventType<JSONArray>(EventCodeUtils.ISMODIFY_CLICK_COLUMN,
                            jsonArray,mCurrentColumnPosition));
                    LogUtils.d(TAG, "onback onKeyDown : " + modify + "post : ISMODIFY_CLICK_COLUMN ");
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
