package com.shuzhengit.zhixin.mine;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.permission.AcpListener;
import com.library.permission.AcpOptions;
import com.library.permission.AcpUtils;
import com.library.rx.RxBus2;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.Token;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.BitmapUtils;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CacheUtils;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.DialogUtil;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ImageViewHelper;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditUserInfoActivity extends BaseActivity {
    private static final String TAG = "EditUserInfoActivity";
    private static final int REQUEST_CODE_CHOOSE = 10000;
    @BindViews({R.id.rlNickname, R.id.rlSex, R.id.rlBirthday, R.id.rlSchool})
    RelativeLayout[] mInfoRls;
//    RelativeLayout[] mInfoRls = new RelativeLayout[4];

    @BindView(R.id.tvTitle)
    TextView mTvTitle;

    @BindView(R.id.tvRight)
    TextView mTvRight;

    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.civUserAvatar)
    CircleImageView mCivAvatar;
    @BindView(R.id.rlAvatar)
    RelativeLayout mRlAvatar;
    @BindView(R.id.tvNickname)
    TextView mTvNickname;
    @BindView(R.id.tvEmail)
    TextView mTvEmail;
    @BindView(R.id.rlEmail)
    RelativeLayout mRlEmail;
    @BindView(R.id.tvBirthday)
    TextView mTvBirthday;
    @BindView(R.id.tvSex)
    TextView mTvSex;
    @BindView(R.id.tvSchool)
    TextView mTvSchool;

    List<Uri> mSelected;

    private Token mToken;
    private User mUser;
    private Bitmap mBitmapAvatar;
    private Unbinder mUnbinder;

    @Override
    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart(TAG);
        //统计时长
        MobclickAgent.onResume(this);
        mUser = CheckUser.checkUserIsExists();
        if (mUser != null) {
            if (mUser.getSchoolName() != null) {
                mTvSchool.setText(mUser.getSchoolName());
            } else {
                mTvSchool.setText("");
            }
        }

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
    public void onBackPressed() {
        super.onBackPressed();
        RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.REFRESH_USER_INT, "refresh"));
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_edit_user_info;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        mTvTitle.setText("编辑资料");
        for (int i = 0; i < mInfoRls.length; i++) {
            TextView tv = (TextView) mInfoRls[i].getChildAt(0);
            SpannableString spannableString = new SpannableString(" *");
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.append(spannableString);
        }
        mInfoRls[1].setOnClickListener(v -> {
            int defaultSex = mUser.getGender();
            DialogUtil.showSexDialog(this, defaultSex, new DialogUtil.DialogInputCallBack() {
                @Override
                public void callBackPositive(DialogInterface dialogInterface, String callbackMessage) {
                    mTvSex.setText(callbackMessage);
                    int modifySex = 0;
                    if ("男".equals(callbackMessage)) {
                        modifySex = 1;
                    } else {
                        modifySex = 0;
                    }

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("gender", modifySex);
                        jsonObject.put("modifyType", "gender");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            jsonObject.toString());
                    HttpProtocol.getApi()
                            .modifyUserInfoGender(mUser.getId() + "", requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .subscribe(new RxSubscriber<BaseCallModel<Double>>(EditUserInfoActivity.this) {
                                @Override
                                protected void _onNext(BaseCallModel<Double> callModel) {
//                                    int gender = ((Double) callModel.getData()).intValue();
                                    mUser.setGender(callModel.getData().intValue());
                                    CacheUtils.getCacheManager(EditUserInfoActivity.this).remove("user");
                                    CacheUtils.getCacheManager(EditUserInfoActivity.this).put("user", mUser);
                                    RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.REFRESH_USER_INT,
                                            "refresh"));
                                }
                            });
                }
            });
        });
        mInfoRls[0].setOnClickListener(v -> {
            DialogUtil.showEditTextDialog(this, "修改昵称", "请输入昵称", "昵称不能为空", true, false, new DialogUtil
                    .DialogInputCallBack() {

                @Override
                public void callBackPositive(DialogInterface dialog, String inputMessage) {
                    LogUtils.i(inputMessage);
                    mTvNickname.setText(inputMessage);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", mUser.getId());
                        jsonObject.put("nickname", inputMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            jsonObject.toString());
                    HttpProtocol.getApi()
                            .modifyNickName(requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .subscribe(new RxSubscriber<BaseCallModel<User>>() {
                                @Override
                                protected void _onNext(BaseCallModel<User> userBaseCallModel) {
                                    //failure(userBaseCallModel.getMessage());
                                    LogUtils.i(TAG, userBaseCallModel.toString());
                                    mUser.setNickname(String.valueOf(userBaseCallModel.getData().getNickname()));
                                    CacheUtils.getCacheManager(EditUserInfoActivity.this).remove(CacheKeyManager.USER);
                                    CacheUtils.getCacheManager(EditUserInfoActivity.this).put(CacheKeyManager.USER,
                                            mUser);
                                }
                            });
                }
            });
        });
        mInfoRls[2].setOnClickListener(v -> {
            DialogUtil.showDatePickerDialog(EditUserInfoActivity.this, new DialogUtil.DialogInputCallBack() {
                @Override
                public void callBackPositive(DialogInterface dialogInterface, String callbackMessage) {
                    mTvBirthday.setText(callbackMessage);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("birthday", callbackMessage);
                        jsonObject.put("modifyType", "birthday");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            jsonObject.toString());
                    HttpProtocol.getApi()
                            .modifyUserInfo(mUser.getId() + "", requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .subscribe(new RxSubscriber<BaseCallModel<Object>>(EditUserInfoActivity.this) {
                                @Override
                                protected void _onNext(BaseCallModel<Object> callModel) {
                                    //failure(callModel.getMessage());
                                    mUser.setBirthday(String.valueOf(callModel.getData()));
                                    CacheUtils.getCacheManager(EditUserInfoActivity.this).remove("user");
                                    CacheUtils.getCacheManager(EditUserInfoActivity.this).put("user", mUser);
                                    RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.REFRESH_USER_INT,
                                            "refresh"));
                                }
                            });
                }
            });
        });
        mRlEmail.setOnClickListener(v -> {
            DialogUtil.showEditTextDialog(this, "修改邮箱", "请输入邮箱", "请输入正确的邮箱", true, true, new DialogUtil
                    .DialogInputCallBack() {
                @Override
                public void callBackPositive(DialogInterface dialog, String inputMessage) {
                    LogUtils.i(inputMessage);
                    mTvEmail.setText(inputMessage);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", inputMessage);
                        jsonObject.put("modifyType", "email");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            jsonObject.toString());
                    HttpProtocol.getApi()
                            .modifyUserInfo(mUser.getId() + "", requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .subscribe(new RxSubscriber<BaseCallModel<Object>>(EditUserInfoActivity.this) {
                                @Override
                                protected void _onNext(BaseCallModel<Object> callModel) {
                                    //failure(callModel.getMessage());
                                    mUser.setEmail(String.valueOf(callModel.getData()));
                                    CacheUtils.getCacheManager(EditUserInfoActivity.this).remove("user");
                                    CacheUtils.getCacheManager(EditUserInfoActivity.this).put("user", mUser);
                                    RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.REFRESH_USER_INT,
                                            "refresh"));
                                }
                            });
                }
            });
        });
        mRlAvatar.setOnClickListener(v -> {
            AcpUtils.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission
                    .READ_EXTERNAL_STORAGE).build(), new AcpListener() {
                @Override
                public void onGranted() {
                    Matisse.from(EditUserInfoActivity.this)
                            .choose(MimeType.allOf())
                            .countable(true)
                            .maxSelectable(1)
                            .gridExpectedSize(360)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(REQUEST_CODE_CHOOSE);
                }

                @Override
                public void onDenied(List<String> permissions) {

                }
            });
        });
        mInfoRls[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SelectSchoolActivity.class);
            }
        });
    }

    @Override
    protected void createPresenter() {
        mToken = (Token) CacheUtils.getCacheManager(this).getAsObject(CacheKeyManager.TOKEN);
        mUser = (User) CacheUtils.getCacheManager(this).getAsObject(CacheKeyManager.USER);
        String avatarUrl = mUser.getAvatarUrl();
        if (!TextUtils.isEmpty(avatarUrl)) {
            GlideLoadImageUtils.loadImg(this, avatarUrl, mCivAvatar);
        } else {
            mCivAvatar.setImageResource(R.drawable.ic_normal_icon);
        }
        mTvNickname.setText(mUser.getNickname());
        mTvEmail.setText(mUser.getEmail());
        mTvSex.setText(mUser.getGender() == 0 ? "女" : "男");
        mTvBirthday.setText(mUser.getBirthday());
        mTvSchool.setText(mUser.getSchoolName());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Uri uri = mSelected.get(0);
            mBitmapAvatar = BitmapUtils.decodeUri(EditUserInfoActivity.this, uri, ImageViewHelper.getImageViewWidth
                            (mCivAvatar),
                    ImageViewHelper.getImageViewHeight(mCivAvatar));
            if (mBitmapAvatar != null) {
                mCivAvatar.setImageBitmap(mBitmapAvatar);
            }
            updateAvatar();
        }

    }

    private void updateAvatar() {
        if (mBitmapAvatar == null) {
            return;
        }
        byte[] bytes = BitmapUtils.compressImageToByte(mBitmapAvatar);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
        HttpProtocol.getApi()
                .updateAvatar(mUser.getId() + "", requestBody)
                .compose(RxSchedulersHelper.io_main())
                .subscribe(new RxSubscriber<BaseCallModel<String>>(this) {
                    @Override
                    protected void _onNext(BaseCallModel<String> avatar) {
                        failure("修改头像成功");
                        mUser.setAvatarUrl(avatar.getData());
                        CacheUtils.getCacheManager(EditUserInfoActivity.this).remove("user");
                        CacheUtils.getCacheManager(EditUserInfoActivity.this).put("user", mUser);
                        GlideLoadImageUtils.loadImg(EditUserInfoActivity.this, avatar.getData(), mCivAvatar);
                        RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.REFRESH_USER_INT, "refresh"));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
