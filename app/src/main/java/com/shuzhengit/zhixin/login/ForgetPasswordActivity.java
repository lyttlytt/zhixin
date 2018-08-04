package com.shuzhengit.zhixin.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.DeviceUtil;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ForgetPasswordActivity extends BaseActivity {
    private static final String TAG = "ForgetPasswordActivity";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.etValidateCode)
    EditText mEtValidateCode;
    @BindView(R.id.btnSendCode)
    Button mBtnSendCode;
    private CountDownTimer mCountDownTimer;
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
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
//        mTvTitle = (TextView) findViewById(R.id.tvTitle);
//        mToolbar = (Toolbar) findViewById(R.id.toolBar);
//        mEtPhone = (EditText) findViewById(R.id.etPhone);
//        mEtPassword = (EditText) findViewById(R.id.etPassword);
//        mEtValidateCode = (EditText) findViewById(R.id.etValidateCode);
//        mBtnSendCode = (Button) findViewById(R.id.btnSendCode);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvTitle.setText("找回密码");
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        mBtnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEtPhone.getText().toString().trim();
                boolean mobileNO = DeviceUtil.isMobileNO(phone);
                if (!mobileNO) {
                    failure("请输入正确的手机号码");
                    return;
                }
                HttpProtocol.getApi()
                        .sendResetPasswordValidCode(phone)
                        .compose(RxSchedulersHelper.io_main())
                        .subscribe(new RxSubscriber<BaseCallModel>() {
                            @Override
                            protected void _onNext(BaseCallModel callModel) {

                            }
                        });
                mCountDownTimer.start();
            }
        });
    }

    @Override
    protected void createPresenter() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBtnSendCode.setClickable(false);
                mBtnSendCode.setText(String.valueOf((millisUntilFinished / 1000) + "秒后重新发送"));
                mBtnSendCode.setEnabled(false);
//                mBtnSendCode.setBackgroundColor(ResourceUtils.getResourceColor(RegisterActivity.this,R.color
// .blueGrey500));
            }

            @Override
            public void onFinish() {
                mBtnSendCode.setClickable(true);
                mBtnSendCode.setText("发送验证码");
//                mBtnSendCode.setBackgroundColor(ResourceUtils.getResourceColor(RegisterActivity.this,R.color
// .blue700));
                mBtnSendCode.setEnabled(true);

            }
        };
    }

    public void complete(View view) {
        String phone = mEtPhone.getText().toString().trim();
        boolean mobileNO = DeviceUtil.isMobileNO(phone);
        if (!mobileNO) {
            failure("请输入正确的手机号码");
            return;
        }
        String password = mEtPassword.getText().toString().trim();
        if (password.length() < 6) {
            failure("密码不能少与6位");
            return;
        }
        String verificationCode = mEtValidateCode.getText().toString().trim();
        if (TextUtils.isEmpty(verificationCode) || verificationCode.length() != 4) {
            failure("验证码不能为空或不等于4位");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phone);
            jsonObject.put("password", password);
            jsonObject.put("validCode", verificationCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString
                ());
        HttpProtocol.getApi()
                .putResetPassword(body)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribe(new RxSubscriber<User>(this) {
                    @Override
                    protected void _onNext(User user) {
                        failure("重置密码成功,赶紧登录吧");
                        onBackPressed();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
