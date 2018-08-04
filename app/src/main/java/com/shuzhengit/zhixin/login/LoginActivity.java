package com.shuzhengit.zhixin.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.util.AppManager;
import com.library.util.DeviceUtil;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.HomeActivity;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.mine.EditUserInfoActivity;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.KeyBorderLeakUtil;
import com.shuzhengit.zhixin.util.ShareMediaAvailableUtils;
import com.shuzhengit.zhixin.view.SocializeLoadingDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.password)
    TextView mPassword;
    @BindView(R.id.register)
    TextView mRegister;
    @BindView(R.id.imageView)
    ImageView mImageView;
    private SocializeLoadingDialog mSocializeLoadingDialog;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mSocializeLoadingDialog = new SocializeLoadingDialog(this);
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, null);
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, null);
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new LoginPresenter(this);
    }


    public void login(View v) {
        String phone = mEtPhone.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        if (!DeviceUtil.isMobileNO(phone)) {
            failure("账号错误,请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            failure("密码不能少于6位!");
            return;
        }
        mBasePresenter.normalLogin(phone, password);
    }

    public void register(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void forgetPassword(View v) {
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
    }

    public void qqLogin(View v) {
//        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, null);
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, new MyUMAuthListener());
    }

    public void wechatLogin(View v) {
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, null);
        boolean weChatAvailable = ShareMediaAvailableUtils.isWeChatAvailable(this);
        if (weChatAvailable) {
            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new MyUMAuthListener());
        } else {
            failure("没有找到微信,请先安装微信");
        }
    }

    public void sinaLogin(View v) {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, new MyUMAuthListener());
    }

    @Override
    public void onLoginSuccess(User user) {
//        Flowable.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(FlowableEmitter<String> e) throws Exception {
//                HttpProtocol.getApi()
//                        .getIP(APP.USER_AGENT)
//                        .subscribeWith(new DisposableSubscriber<IPModel>() {
//                            @Override
//                            public void onNext(IPModel ipModel) {
//                                if (ipModel.getData() != null) {
//                                    e.onNext(ipModel.getData().getIp());
//                                } else {
//                                    e.onError(new Throwable("ip cannot be null"));
//                                }
//                                e.onComplete();
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                e.onError(t);
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                e.onComplete();
//                            }
//                        });
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
////                .compose(RxSchedulersHelper.io_main())
//                .flatMap(new Function<String, Publisher<RequestBody>>() {
//                    @Override
//                    public Publisher<RequestBody> apply(String s) throws Exception {
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("lastLoginIp",s);
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
//                        String format = sdf.format(System.currentTimeMillis());
//                        jsonObject.put("lastLoginTime",format);
//                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject
//                                .toString());
//                        return Flowable.create(new FlowableOnSubscribe<RequestBody>() {
//                            @Override
//                            public void subscribe(FlowableEmitter<RequestBody> e) throws Exception {
//                                e.onNext(requestBody);
//                                e.onComplete();
//                            }
//                        }, BackpressureStrategy.BUFFER);
//                    }
//                })
//                .subscribeWith(new RxSubscriber<RequestBody>() {
//                    @Override
//                    protected void _onNext(RequestBody s) {
//                        HttpProtocol.getApi()
//                                .postUserData(user.getId(),s)
//                                .compose(RxSchedulersHelper.io_main())
//                                .subscribeWith(new RxSubscriber<BaseCallModel>() {
//                                    @Override
//                                    protected void _onNext(BaseCallModel callModel) {
//
//                                    }
//                                });
//                    }
//                });
        AppManager.finishActivity(HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EventCodeUtils.REFRESH_USER, 1);
        startActivity(HomeActivity.class, bundle);
//        onBackPressed();
    }

    @Override
    public void onLoginSuccessNeedEditUserInfo() {
        startActivityAndKill(EditUserInfoActivity.class);
        AppManager.finishActivity(HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EventCodeUtils.REFRESH_USER, 1);
        startActivity(HomeActivity.class, bundle);
    }


    private class MyUMAuthListener implements UMAuthListener {

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            mSocializeLoadingDialog.show();
        }
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            LogUtils.i("socialize login onComplete");
            LogUtils.e(map.toString());
            if (share_media.equals(SHARE_MEDIA.QQ)) {
                mBasePresenter.socialLogin(map.get("openid"), map.get("openid"), "thirdparty", map.get
                        ("access_token"), "qq");
            }
            if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                mBasePresenter.socialLogin(map.get("openid"), map.get("openid"), "thirdparty", map.get
                        ("access_token"), "weixin");
            }
            if (share_media.equals(SHARE_MEDIA.SINA)) {
                mBasePresenter.socialLogin(map.get("uid"), map.get("uid"), "thirdparty", map.get("access_token"),
                        "weibo");
            }
            mSocializeLoadingDialog.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            LogUtils.i("socialize login onError");
            failure(throwable.getLocalizedMessage());
            mSocializeLoadingDialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            LogUtils.i("socialize login onCancel");
            mSocializeLoadingDialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
}
