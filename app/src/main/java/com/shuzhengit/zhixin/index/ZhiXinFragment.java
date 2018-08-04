package com.shuzhengit.zhixin.index;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.library.base.BaseLazyLoadFragment;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.column.ColumnActivity;
import com.shuzhengit.zhixin.index.document.DocumentFragment;
import com.shuzhengit.zhixin.index.document.local.LocalCityFragment;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.view.ColorTrackTabLayout;
import com.shuzhengit.zhixin.view.PromptLoginDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/18 17:45
 * E-mail:yuancongbin@gmail.com
 */

public class ZhiXinFragment extends BaseLazyLoadFragment<ZhiXinContract.Presenter> implements ZhiXinContract.View {
    private static final String TAG = "ZhiXinFragment";
    @BindView(R.id.columnTabLayout)
    ColorTrackTabLayout mColumnTabLayout;
    @BindView(R.id.ivColumn)
    ImageView mIvColumn;
    @BindView(R.id.documentContentViewPager)
    ViewPager mDocumentContentViewPager;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    private List<Column> mColumns;
    private Unbinder mUnbinder;
    private ZhiXinViewPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhixin;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
    }


    @Override
    public void onPause() {
        super.onPause();
        //统计页面
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    protected void lazyLoadCreatePresenter() {
        MobclickAgent.onPageStart(TAG);
        mBasePresenter = new ZhiXinPresenter(this);
        mBasePresenter.start();
        User user = CheckUser.checkUserIsExists();
        if (user != null) {
            mBasePresenter.findColumns(user.getId());

            mIvColumn.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                int currentItem = mDocumentContentViewPager.getCurrentItem();
                Column column = mColumns.get(currentItem);
                if (column.getId() != null) {
                    bundle.putInt("currentColumnPosition", column.getId());
                } else {
                    bundle.putInt("currentColumnPosition", -1);
                }
                startActivity(ColumnActivity.class, bundle);
            });
        } else {
            mBasePresenter.findColumns(0);
            mIvColumn.setOnClickListener(v -> {
                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(getContext());
                promptLoginDialog.show();
                promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                    @Override
                    public void callBackPositive(DialogInterface dialog) {
                        startActivity(LoginActivity.class);
                    }
                });
            });
        }
        mIvColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = CheckUser.checkUserIsExists();
                if (u != null && mDocumentContentViewPager != null && mColumns != null) {
                    Bundle bundle = new Bundle();
                    int currentItem = mDocumentContentViewPager.getCurrentItem();
                    Column column = mColumns.get(currentItem);
                    if (column.getId() != null) {
                        bundle.putInt("currentColumnPosition", column.getId());
                    } else {
                        bundle.putInt("currentColumnPosition", -1);
                    }
                    startActivity(ColumnActivity.class, bundle);
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(getContext());
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
    public void findColumnSuccess(List<Column> columns, List<Fragment> fragments) {
        mColumns = columns;
        mAdapter = new ZhiXinViewPagerAdapter(getChildFragmentManager(), fragments) {
            @Override
            public CharSequence getPageTitle(int position) {
                return columns.get(position).getColumnTitle();
            }
        };
        mDocumentContentViewPager.setAdapter(mAdapter);
        mDocumentContentViewPager.setOffscreenPageLimit(1);
        mColumnTabLayout.setupWithViewPager(mDocumentContentViewPager);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void findColumnSuccessWithCurrentPage(List<Column> columns, List<Fragment> fragments, int columnId) {
        mColumns = columns;
        mAdapter = new ZhiXinViewPagerAdapter(getChildFragmentManager(), fragments) {
            @Override
            public CharSequence getPageTitle(int position) {
                return columns.get(position).getColumnTitle();
            }
        };
        mDocumentContentViewPager.setAdapter(mAdapter);
//        mDocumentContentViewPager.setCurrentItem(position);
//        List<Fragment> fragments = new ArrayList<>();
//        for (int i = 0; i < mNewsTabs.length; i++) {
//            fragments.add(DocumentFragment.getInstance(mNewsTabs[i]));
//        }
//        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getChildFragmentManager(), fragments,
//                mNewsTabs);
//        mNewsContentViewPager.setAdapter(adapter);
        //来回切换是否需要重新加载 不要的话走这个 全部不会重新加载
//        mNewsContentViewPager.setOffscreenPageLimit(fragments.size());
        //来回切换 最少一个不需要重新加载
//        mNewsContentViewPager.setOffscreenPageLimit(1);
        mColumnTabLayout.setupWithViewPager(mDocumentContentViewPager);
        int position = 0;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getId() != null && columns.get(i).getId() == columnId) {
                position = i;
                break;
            }
        }
        mDocumentContentViewPager.setCurrentItem(position);
    }

    @Override
    public void switchPage(int position) {
        int i = 0;
        mDocumentContentViewPager.setCurrentItem(position);
    }

    public ViewPager getDocumentContentViewPager() {
        return mDocumentContentViewPager;
    }

    public void startSmoothTop() {
        if (mDocumentContentViewPager != null) {
            int currentItem = mDocumentContentViewPager.getCurrentItem();
            if (currentItem >= 0 && mAdapter != null) {
                Fragment item = mAdapter.getItem(currentItem);
                if (item instanceof LocalCityFragment) {
                    ((LocalCityFragment) item).smoothTop();
                } else {
                    ((DocumentFragment) item).smoothTop();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
