package com.shuzhengit.zhixin.wenba;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.rx.RxBus2;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.EventType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditApplyAdminInfoActivity extends BaseActivity {
    public static final int EDIT_RESUME = 0;
    public static final int EDIT_PROFESSION = 1;
    public static final int EDIT_HOME_WELCOME = 2;
    public static final String EDIT_MODE = "editMode";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.etInfo)
    EditText mEtInfo;
    private Unbinder mUnbinder;
    private int mIntExtra;

    @Override
    protected int layoutId() {
        return R.layout.activity_edit_apply_admin_info;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
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
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("完成");
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEtInfo.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    failure("内容不能为空");
                    return;
                } else {
                    EventType<String> eventType;
                    switch (mIntExtra) {
                        case EDIT_RESUME:
                            eventType = new EventType<>(EDIT_RESUME, s);
                            RxBus2.getDefault().post(eventType);
                            break;
                        case EDIT_PROFESSION:
                            eventType = new EventType<>(EDIT_PROFESSION, s);
                            RxBus2.getDefault().post(eventType);
                            break;
                        case EDIT_HOME_WELCOME:
                            eventType  = new EventType<>(EDIT_HOME_WELCOME, s);
                            RxBus2.getDefault().post(eventType);
                            break;
                        default:
                            break;
                    }
                    onBackPressed();
                }
            }
        });
    }

    @Override
    protected void createPresenter() {
        mIntExtra = getIntent().getIntExtra(EDIT_MODE, EDIT_RESUME);
        switch (mIntExtra) {
            case EDIT_RESUME:
                mEtInfo.setHint("介绍一下你自己吧.例:我是一名计算机专业的学生,擅长修电脑.");
                break;
            case EDIT_PROFESSION:
                mEtInfo.setHint("写下你的职业");
                break;
            case EDIT_HOME_WELCOME:
                mEtInfo.setHint("问吧欢迎语.例:我是xxx,有关电脑装系统的问题,问我吧!");
                break;
            default:
                failure("请选择编辑的类型");
                onBackPressed();
                break;
        }
        String data = getIntent().getStringExtra("data");
        mEtInfo.setText(data);

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
