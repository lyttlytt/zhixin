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
import com.library.rx.RxSchedulersHelper;
import com.library.util.DeviceUtil;
import com.shuzhengit.zhixin.R;
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

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.etCode)
    EditText mEtCode;
    @BindView(R.id.btnSendCode)
    Button mBtnSendCode;
    private CountDownTimer mCountDownTimer;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mEtPhone = (EditText) findViewById(R.id.etPhone);
        mEtPassword = (EditText) findViewById(R.id.etPassword);
        mEtCode = (EditText) findViewById(R.id.etCode);
        mBtnSendCode = (Button) findViewById(R.id.btnSendCode);


        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvTitle.setText("注册");
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
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

    public void register(View view) {
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
        String verificationCode = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(verificationCode) || verificationCode.length() != 4) {
            failure("验证码不能为空或不等于4位");
            return;
        }
//        mobile	true	普通参数	string		手机号码
//        email	false	普通参数	string		邮箱
//        type	true	普通参数	string		注册方式1：手机，2：邮箱
//        password	true	普通参数	string		登录密码
//        nickName	false	普通参数	string		默认昵称，选填
//        validateCode true  string  短信验证码
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phone);
            jsonObject.put("type", 1);
            jsonObject.put("password", password);
            jsonObject.put("validCode", verificationCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject
                .toString());
        HttpProtocol.getApi()
                .register(requestBody)
                .compose(RxSchedulersHelper.io_main())
                .subscribe(new RxSubscriber<BaseCallModel>(this) {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {
                        if (callModel.isOk()) {
                            failure("注册成功,赶紧登录吧!");
                            onBackPressed();
                        } else {
                            if (callModel.code == 400) {
                                failure("账号已存在");
                            } else {
                                failure(callModel.getMessage());
                            }
                        }
                    }
                });
    }

    public void sendCode(View view) {
        String phone = mEtPhone.getText().toString().trim();
        if (!DeviceUtil.isMobileNO(phone)) {
            failure("请输入正确的手机号码!");
            return;
        }
        HttpProtocol
                .getApi()
                .sendCode(phone)
                .compose(RxSchedulersHelper.io_main())
                .subscribe(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {

                    }
                });
        mCountDownTimer.start();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
