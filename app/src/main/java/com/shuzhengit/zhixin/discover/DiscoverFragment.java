package com.shuzhengit.zhixin.discover;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseLazyLoadFragment;
import com.shuzhengit.zhixin.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/18 17:47
 * E-mail:yuancongbin@gmail.com
 */

public class DiscoverFragment extends BaseLazyLoadFragment {
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;

    private static final String TAG = "DiscoverFragment";
    private Unbinder mUnbinder;

    @Override
    public void onResume() {
        super.onResume();
        //统计页面
//          MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        //统计页面
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this,view);
        mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
        mToolBar = (Toolbar) view.findViewById(R.id.toolBar);

        ((TextView) view.findViewById(R.id.itemQuestion).findViewById(R.id.tvItemName)).setText("问答");
        ((ImageView) view.findViewById(R.id.itemQuestion).findViewById(R.id.ivLeftIcon)).setImageResource(R.drawable
                .ic_question_unselect);
        view.findViewById(R.id.itemQuestion).setOnClickListener(v -> failure(R.string.unFinish));

        ((TextView) view.findViewById(R.id.itemMajor).findViewById(R.id.tvItemName)).setText("专业");
        ((ImageView) view.findViewById(R.id.itemMajor).findViewById(R.id.ivLeftIcon)).setImageResource(R.drawable
                .ic_mine_major);
        view.findViewById(R.id.itemMajor).setOnClickListener(v -> failure(R.string.unFinish));

        ((TextView) view.findViewById(R.id.itemDataShare).findViewById(R.id.tvItemName)).setText("资料分享");
        ((ImageView) view.findViewById(R.id.itemDataShare).findViewById(R.id.ivLeftIcon)).setImageResource(R.drawable
                .ic_question_unselect);
        view.findViewById(R.id.itemDataShare).setOnClickListener(v -> failure(R.string.unFinish));

        ((TextView) view.findViewById(R.id.itemIdle).findViewById(R.id.tvItemName)).setText("闲置物品");
        ((ImageView) view.findViewById(R.id.itemIdle).findViewById(R.id.ivLeftIcon)).setImageResource(R.drawable
                .ic_question_unselect);
        view.findViewById(R.id.itemIdle).setOnClickListener(v -> failure(R.string.unFinish));

        ((TextView) view.findViewById(R.id.itemCommunity).findViewById(R.id.tvItemName)).setText("社团活动");
        ((ImageView) view.findViewById(R.id.itemCommunity).findViewById(R.id.ivLeftIcon)).setImageResource(R.drawable
                .ic_question_unselect);
        view.findViewById(R.id.itemCommunity).setOnClickListener(v -> failure(R.string.unFinish));

        ((TextView) view.findViewById(R.id.itemEat).findViewById(R.id.tvItemName)).setText("吃喝玩乐");
        ((ImageView) view.findViewById(R.id.itemEat).findViewById(R.id.ivLeftIcon)).setImageResource(R.drawable
                .ic_question_unselect);
        view.findViewById(R.id.itemEat).setOnClickListener(v -> failure(R.string.unFinish));


        getHoldingActivity().setSupportActionBar(mToolBar);
        ActionBar actionBar = getHoldingActivity().getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mTvTitle.setText("发现");
    }

    @Override
    protected void lazyLoadCreatePresenter() {
        MobclickAgent.onPageStart(TAG);

    }
}
