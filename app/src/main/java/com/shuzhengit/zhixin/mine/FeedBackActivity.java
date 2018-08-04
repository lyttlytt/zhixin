package com.shuzhengit.zhixin.mine;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.KeyBorderLeakUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FeedBackActivity extends BaseActivity {
    private static final String TAG = "FeedBackActivity";
//    TextView mTvTitle;
//    TextView mTvRight;
//    ImageView mIvRightShare;
//    Toolbar mToolBar;
//    EditText mEtFeedBack;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.etFeedBack)
    EditText mEtFeedBack;
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
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
//        mTvTitle = (TextView) findViewById(R.id.tvTitle);
//        mTvRight = (TextView) findViewById(R.id.tvRight);
//        mIvRightShare = (ImageView) findViewById(R.id.ivRightShare);
//        mToolBar = (Toolbar) findViewById(R.id.toolBar);
//        mEtFeedBack = (EditText) findViewById(R.id.etFeedBack);


        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvTitle.setText("问题反馈");
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void createPresenter() {

    }

    public void sendFeedBack(View view) {
        String s = mEtFeedBack.getText().toString();
        if (TextUtils.isEmpty(s)) {
            failure("内容不能为空哦");
            return;
        }
        User user = CheckUser.checkUserIsExists();
        if (user!=null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("author", user.getId());
                jsonObject.put("content", s);
                jsonObject.put("status", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        HttpProtocol.getApi()
                .sendFeedback(requestBody)
                .compose(RxSchedulersHelper.io_main())
                .subscribe(new RxSubscriber<BaseCallModel>(this) {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {
                        if (callModel.code == 200 && callModel.isOk()) {
                            mEtFeedBack.setText("");
                            failure("发送成功");
                        }
                    }
                });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            KeyBorderLeakUtil.fixFocusedViewLeak(APP.getInstance());
        }
    }
}
