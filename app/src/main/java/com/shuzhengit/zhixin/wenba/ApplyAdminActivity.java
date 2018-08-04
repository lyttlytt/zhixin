package com.shuzhengit.zhixin.wenba;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.download.UploadFileRequestBody;
import com.library.download.UploadListener;
import com.library.permission.AcpListener;
import com.library.permission.AcpOptions;
import com.library.permission.AcpUtils;
import com.library.rx.RxBus2;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.library.util.Retrofit2ConverterFactory;
import com.library.util.ToastUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.ApplyAdmin;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.ModifyBarInfo;
import com.shuzhengit.zhixin.bean.UploadImg;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.ApiService;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.BitmapUtils;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.DialogUtil;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.SocializeLoadingDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Predicate;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class ApplyAdminActivity extends BaseActivity {
    private static final String TAG = "ApplyAdminActivity";

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.civUserAvatar)
    CircleImageView mCivUserAvatar;
    @BindView(R.id.rlAvatar)
    RelativeLayout mRlAvatar;
    @BindView(R.id.tvNickname)
    TextView mTvNickname;
    @BindView(R.id.rlNickname)
    RelativeLayout mRlNickname;
    @BindView(R.id.tvResume)
    TextView mTvResume;
    @BindView(R.id.rlResume)
    RelativeLayout mRlResume;
    @BindView(R.id.tvProfession)
    TextView mTvProfession;
    @BindView(R.id.rlProfession)
    RelativeLayout mRlProfession;
    @BindView(R.id.rlHomeImg)
    RelativeLayout mRlHomeImg;
    @BindView(R.id.tvWelCome)
    TextView mTvWelCome;
    @BindView(R.id.rlWelcome)
    RelativeLayout mRlWelcome;
    @BindView(R.id.rlCategory)
    RelativeLayout mRlCategory;
    @BindView(R.id.tvCategory)
    TextView mTvCategory;
    //    @BindView(R.id.tvReview)
//    TextView mTvReview;
    @BindView(R.id.tvFail)
    TextView mTvFail;
    @BindView(R.id.btnApply)
    Button mBtnApply;
    @BindView(R.id.ivThumbnail)
    ImageView mIvThumbnail;
    private Unbinder mUnbinder;
    private Column mQuestionCategory;
    private int mCategoryId = -1;
    List<Uri> mSelected = new ArrayList<>();
    private UploadImg mUploadImg;
    private String mHomeImg;
    private User mUser;
    private ApiService mApiService;
    private static final int UPLOADING = 1;
    private static final int UPLOAD_SUCCESS = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPLOADING:
                    if (!mSocializeLoadingDialog.isShowing()) {
                        mSocializeLoadingDialog.show();
                    }
                    break;
                case UPLOAD_SUCCESS:
                    mSocializeLoadingDialog.dismiss();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };
    private SocializeLoadingDialog mSocializeLoadingDialog;
    private RxSubscriber<EventType> mRxSubscriber;
    private boolean mIsModify;

    @Override
    protected int layoutId() {
        return R.layout.activity_apply_admin;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mIsModify = getIntent().getBooleanExtra("isModify", false);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mApiService = initHttp();
        if (!mIsModify) {
            mTvTitle.setText("申请吧主");
            mBtnApply.setText("申请");
        } else {
            mTvTitle.setText("修改信息");
            mBtnApply.setText("提交审核");
        }
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mRlResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(EditApplyAdminInfoActivity.EDIT_MODE, EditApplyAdminInfoActivity.EDIT_RESUME);
                String s = mTvResume.getText().toString();
                bundle.putString("data", s);
                startActivity(EditApplyAdminInfoActivity.class, bundle);
            }
        });
        mRlProfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(EditApplyAdminInfoActivity.EDIT_MODE, EditApplyAdminInfoActivity.EDIT_PROFESSION);
                String s = mTvProfession.getText().toString();
                bundle.putString("data", s);
                startActivity(EditApplyAdminInfoActivity.class, bundle);
            }
        });
        mRlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SelectCategoryActivity.class);
            }
        });
        mRlWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(EditApplyAdminInfoActivity.EDIT_MODE, EditApplyAdminInfoActivity.EDIT_HOME_WELCOME);
                String s = mTvWelCome.getText().toString();
                bundle.putString("data", s);
                startActivity(EditApplyAdminInfoActivity.class, bundle);
            }
        });
        mIvThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcpUtils.getInstance(APP.getInstance())
                        .request(new AcpOptions.Builder().setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
                                Matisse.from(ApplyAdminActivity.this)
                                        .choose(MimeType.allOf())
                                        .countable(true)
                                        .gridExpectedSize(360)
                                        .maxSelectable(1)
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f)
                                        .imageEngine(new GlideEngine())
                                        .forResult(10000);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {

                            }
                        });
            }
        });

        mBtnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resume = mTvResume.getText().toString();
                if (TextUtils.isEmpty(resume)) {
                    failure("简介不能为空");
                    return;
                }
                if (mCategoryId == -1) {
                    failure("请选择分类");
                    return;
                }
                String profession = mTvProfession.getText().toString();
                if (TextUtils.isEmpty(profession)) {
                    failure("请填写你的职业");
                    return;
                }
                if (TextUtils.isEmpty(mHomeImg)) {
                    failure("请上传您的问吧宣传图片");
                    return;
                }
                String welcome = mTvWelCome.getText().toString();
                if (TextUtils.isEmpty(welcome)) {
                    failure("请填写问吧欢迎语句");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", mUser.getId());
                jsonObject.put("barImage", mHomeImg);
                jsonObject.put("description", resume);
                jsonObject.put("profession", profession);
                jsonObject.put("welcomeTitle", welcome);
                jsonObject.put("categoryId", mCategoryId);
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonObject
                        .toString());
                if (!mIsModify) {
//                    String resume = mTvResume.getText().toString();
//                    if (TextUtils.isEmpty(resume)) {
//                        failure("简介不能为空");
//                        return;
//                    }
//                    if (mCategoryId == -1) {
//                        failure("请选择分类");
//                        return;
//                    }
//                    String profession = mTvProfession.getText().toString();
//                    if (TextUtils.isEmpty(profession)) {
//                        failure("请填写你的职业");
//                        return;
//                    }
//                    if (TextUtils.isEmpty(mHomeImg)) {
//                        failure("请上传您的问吧宣传图片");
//                        return;
//                    }
//                    String welcome = mTvWelCome.getText().toString();
//                    if (TextUtils.isEmpty(welcome)) {
//                        failure("请填写问吧欢迎语句");
//                        return;
//                    }
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("userId", mUser.getId());
//                    jsonObject.put("barImage", mHomeImg);
//                    jsonObject.put("description", resume);
//                    jsonObject.put("profession", profession);
//                    jsonObject.put("welcomeTitle", welcome);
//                    jsonObject.put("categoryId", mCategoryId);
//                    RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
// jsonObject
//                            .toString());
                    HttpProtocol.getApi()
                            .applyAdmin(body)
                            .compose(RxSchedulersHelper.io_main())
                            .subscribeWith(new RxSubscriber<BaseCallModel>() {
                                @Override
                                protected void _onNext(BaseCallModel callModel) {
                                    if (callModel.code == 200) {
                                        failure("申请成功,审核中");
                                        onBackPressed();
                                    }
                                }
                            });
                } else {
                    DialogUtil.showDialog(ApplyAdminActivity.this, "提示", "确定提交修改信息吗?", true, new DialogUtil
                            .DialogCallBack() {

                        @Override
                        public void callBackNegative(DialogInterface dialog) {
                        }

                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            HttpProtocol.getApi().modifyWenBaInfo(body)
                                    .compose(RxSchedulersHelper.io_main())
                                    .subscribeWith(new RxSubscriber<BaseCallModel>(ApplyAdminActivity.this) {
                                        @Override
                                        protected void _onNext(BaseCallModel callModel) {
                                            Log.d(TAG, "----->"+callModel.toString());
                                            if (callModel.code == 200) {
                                                mTvFail.setVisibility(View.GONE);
                                                mRlCategory.setClickable(false);
                                                mRlProfession.setClickable(false);
                                                mRlResume.setClickable(false);
                                                mRlWelcome.setClickable(false);
                                                mIvThumbnail.setClickable(false);
                                                mBtnApply.setText("审核中,不可修改");
                                                mBtnApply.setClickable(false);
                                            }
                                        }
                                    });
                        }
                    });

                }
            }

        });
    }

    @Override
    protected void createPresenter() {
        mUser = CheckUser.checkUserIsExists();
        if (mUser == null) {
            failure("请先登录");
            onBackPressed();
            return;
        }
        if (TextUtils.isEmpty(mUser.getAvatarUrl())) {
            mCivUserAvatar.setImageResource(R.drawable.ic_normal_icon);
        } else {
            GlideLoadImageUtils.loadImg(this, mUser.getAvatarUrl(), mCivUserAvatar);
        }
        mTvNickname.setText(mUser.getNickname());
        mRxSubscriber = new RxSubscriber<EventType>() {
            @Override
            protected void _onNext(EventType eventType) {
                if (eventType.getEventType().equals(EditApplyAdminInfoActivity.EDIT_RESUME)) {
                    String event = (String) eventType.getEvent();
                    mTvResume.setText(event);
                } else if (eventType.getEventType().equals(EditApplyAdminInfoActivity.EDIT_PROFESSION)) {
                    String event = (String) eventType.getEvent();
                    mTvProfession.setText(event);
                } else if (eventType.getEventType().equals(EditApplyAdminInfoActivity.EDIT_HOME_WELCOME)) {
                    String event = (String) eventType.getEvent();
                    mTvWelCome.setText(event);
                } else if (eventType.getEventType().equals(EventCodeUtils.WEN_BA_CATEGORY)) {
                    mQuestionCategory = (Column) eventType.getEvent();
                    mCategoryId = mQuestionCategory.getId();
                    mTvCategory.setText(mQuestionCategory.getColumnTitle());
                }
            }
        };
        RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EditApplyAdminInfoActivity.EDIT_RESUME) || eventType
                                .getEventType().equals(EditApplyAdminInfoActivity.EDIT_PROFESSION) || eventType
                                .getEventType().equals(EditApplyAdminInfoActivity.EDIT_HOME_WELCOME) || eventType
                                .getEventType().equals(EventCodeUtils.WEN_BA_CATEGORY);
                    }
                })
                .subscribeWith(mRxSubscriber);
        if (mIsModify){
            HttpProtocol.getApi()
                    .queryWenBaStatus(mUser.getId())
                    .compose(RxSchedulersHelper.io_main())
                    .compose(RxResultHelper.handleResult())
                    .subscribeWith(new RxSubscriber<ModifyBarInfo>() {
                        @Override
                        protected void _onNext(ModifyBarInfo modifyBarInfo) {
                            if (modifyBarInfo.getStatus()==2){
                                mTvFail.setVisibility(View.VISIBLE);
                                mTvFail.setText("审核未通过:" + modifyBarInfo.getAuditResult());
                                mTvResume.setText(modifyBarInfo.getDescription());
                                mTvProfession.setText(modifyBarInfo.getProfession());
                                mTvWelCome.setText(modifyBarInfo.getWelcomeTitle());
                                GlideLoadImageUtils.loadImg(ApplyAdminActivity.this, modifyBarInfo.getBarImage(),
                                        mIvThumbnail);
                                mTvCategory.setText(modifyBarInfo.getColumnTitle());
                                mCategoryId = modifyBarInfo.getCategoryId();
                                mHomeImg = modifyBarInfo.getBarImage();
                                mBtnApply.setText("重新提交审核");
                            }else if (modifyBarInfo.getStatus()==0){
                                mTvFail.setVisibility(View.INVISIBLE);
                                mTvResume.setText(modifyBarInfo.getDescription());
                                mTvProfession.setText(modifyBarInfo.getProfession());
                                mTvWelCome.setText(modifyBarInfo.getWelcomeTitle());
                                GlideLoadImageUtils.loadImg(ApplyAdminActivity.this, modifyBarInfo.getBarImage(),
                                        mIvThumbnail);
                                mTvCategory.setText(modifyBarInfo.getColumnTitle());
                                mCategoryId = modifyBarInfo.getCategoryId();
                                mHomeImg = modifyBarInfo.getBarImage();
                                mBtnApply.setText("审核中,不能修改");
                                mRlCategory.setClickable(false);
                                mRlProfession.setClickable(false);
                                mRlResume.setClickable(false);
                                mRlWelcome.setClickable(false);
                                mIvThumbnail.setClickable(false);
                                mBtnApply.setClickable(false);
                            }else {
                                mTvFail.setVisibility(View.INVISIBLE);
                                mTvResume.setText(modifyBarInfo.getDescription());
                                mTvProfession.setText(modifyBarInfo.getProfession());
                                mTvWelCome.setText(modifyBarInfo.getWelcomeTitle());
                                GlideLoadImageUtils.loadImg(ApplyAdminActivity.this, modifyBarInfo.getBarImage(),
                                        mIvThumbnail);
                                mTvCategory.setText(modifyBarInfo.getColumnTitle());
                                mCategoryId = modifyBarInfo.getCategoryId();
                                mHomeImg = modifyBarInfo.getBarImage();
                                mBtnApply.setText("提交审核");
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            HttpProtocol.getApi()
                                    .checkIsAdmin(mUser.getId())
                                    .compose(RxSchedulersHelper.io_main())
                                    .subscribe(new RxSubscriber<BaseCallModel<ApplyAdmin>>() {
                                        @Override
                                        protected void _onNext(BaseCallModel<ApplyAdmin> baseCallModel) {
                                            ApplyAdmin applyAdmin = baseCallModel.getData();
                                            if (applyAdmin!=null){
                                                mTvFail.setVisibility(View.INVISIBLE);
                                                mTvResume.setText(applyAdmin.getDescription());
                                                mTvProfession.setText(applyAdmin.getProfession());
                                                mTvWelCome.setText(applyAdmin.getWelcomeTitle());
                                                GlideLoadImageUtils.loadImg(ApplyAdminActivity.this, applyAdmin.getBarImage(),
                                                        mIvThumbnail);
                                                mTvCategory.setText(applyAdmin.getColumnTitle());
                                                mCategoryId = applyAdmin.getCategoryId();
                                                mHomeImg = applyAdmin.getBarImage();
                                            }
                                        }
                                    });
                        }
                    });
        }else {

        HttpProtocol.getApi()
                .checkIsAdmin(mUser.getId())
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel<ApplyAdmin>>() {
                    @Override
                    protected void _onNext(BaseCallModel<ApplyAdmin> baseCallModel) {
                        ApplyAdmin applyAdmin = baseCallModel.getData();
                        if (applyAdmin != null) {
                            if (applyAdmin.getStatus() == 0) {
//                                mTvReview.setVisibility(View.VISIBLE);
//                                mBtnApply.setVisibility(View.GONE);
                                mTvFail.setVisibility(View.INVISIBLE);
                                mTvResume.setText(applyAdmin.getDescription());
                                mTvProfession.setText(applyAdmin.getProfession());
                                mTvWelCome.setText(applyAdmin.getWelcomeTitle());
                                GlideLoadImageUtils.loadImg(ApplyAdminActivity.this, applyAdmin.getBarImage(),
                                        mIvThumbnail);
                                mTvCategory.setText(applyAdmin.getColumnTitle());
                                mCategoryId = applyAdmin.getCategoryId();
                                mHomeImg = applyAdmin.getBarImage();
                                mRlCategory.setClickable(false);
                                mRlProfession.setClickable(false);
                                mRlResume.setClickable(false);
                                mRlWelcome.setClickable(false);
                                mIvThumbnail.setClickable(false);
                                mBtnApply.setText("审核中,不可修改");
                                mBtnApply.setClickable(false);
                            } else if (applyAdmin.getStatus() == 2) {
//                                mTvReview.setVisibility(View.GONE);
                                mBtnApply.setVisibility(View.VISIBLE);
                                mTvFail.setVisibility(View.VISIBLE);
                                mTvFail.setText("审核未通过:" + applyAdmin.getAuditResult());
                                mTvResume.setText(applyAdmin.getDescription());
                                mTvProfession.setText(applyAdmin.getProfession());
                                mTvWelCome.setText(applyAdmin.getWelcomeTitle());
                                mTvCategory.setText(applyAdmin.getColumnTitle());
                                GlideLoadImageUtils.loadImg(ApplyAdminActivity.this, applyAdmin.getAvatarUrl(),
                                        mIvThumbnail);
                                mHomeImg = applyAdmin.getBarImage();
                                mCategoryId = applyAdmin.getCategoryId();
                            } else {
                                if (!mIsModify) {
                                    ToastUtils.showLongToast(ApplyAdminActivity.this, "你的问吧审核已通过,不能申请多个吧主");
                                    RxBus2.getDefault().post(new EventType<Integer>(EventCodeUtils.REFRESH_WENBA, 0));
                                    onBackPressed();
                                } else {
                                    mTvFail.setVisibility(View.INVISIBLE);
                                    mTvResume.setText(applyAdmin.getDescription());
                                    mTvProfession.setText(applyAdmin.getProfession());
                                    mTvWelCome.setText(applyAdmin.getWelcomeTitle());
                                    GlideLoadImageUtils.loadImg(ApplyAdminActivity.this, applyAdmin.getBarImage(),
                                            mIvThumbnail);
                                    mTvCategory.setText(applyAdmin.getColumnTitle());
                                    mCategoryId = applyAdmin.getCategoryId();
                                    mHomeImg = applyAdmin.getBarImage();
                                }
                            }
                        }
                    }
                });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSocializeLoadingDialog = new SocializeLoadingDialog(ApplyAdminActivity.this);
        if (resultCode == RESULT_OK && requestCode == 10000) {
            mSelected = Matisse.obtainResult(data);
            Bitmap bitmap = BitmapUtils.decodeUri(APP.getInstance(), mSelected.get(0));
            byte[] bytes = BitmapUtils.compressImageToByte(bitmap);
            bitmap.recycle();
//            File file = new File(BitmapUtils.getRealFilePath(ApplyAdminActivity.this, mSelected.get(0)));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
//            UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(requestBody, new UploadListener
// () {
//                @Override
//                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
//                    LogUtils.e("upload image  current : " + bytesWritten + " total : " + contentLength);
////                    socializeLoadingDialog.show();
//                }
//            });
            UploadFileRequestBody body = new UploadFileRequestBody(requestBody, new UploadListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    LogUtils.e("upload image  current : " + bytesWritten + " total : " + contentLength);
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putLong("total", contentLength);
                    bundle.putLong("current", bytesWritten);
                    msg.setData(bundle);
                    msg.what = UPLOADING;
                    mHandler.sendMessage(msg);
                }
            });

            mApiService.uploadImg(body)
                    .compose(RxSchedulersHelper.io_main())
                    .compose(RxResultHelper.handleResult())
                    .subscribeWith(new RxSubscriber<UploadImg>() {
                        @Override
                        protected void _onNext(UploadImg uploadImg) {
                            String url = uploadImg.getUrl();
                            url += "?x-oss-process=style/default";
                            uploadImg.setUrl(url);
                            mUploadImg = uploadImg;
                            mHomeImg = uploadImg.getUrl();
                            mIvThumbnail.setVisibility(View.VISIBLE);
                            GlideLoadImageUtils.loadImg(ApplyAdminActivity.this, url,
                                    mIvThumbnail);
                            Message message = mHandler.obtainMessage(UPLOAD_SUCCESS);
                            mHandler.sendMessage(message);
                        }
                    });
        }
    }

    private ApiService initHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor
                        .Level.BODY))
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request original = chain.request();
//                        Request request = original.newBuilder()
//                                .method(original.method(), new UploadFileRequestBody(original.body(), new
// UploadListener() {
//                                    @Override
//                                    public void onRequestProgress(long bytesWritten, long contentLength, boolean
// done) {
////                                        LogUtils.e("upload image  current : " + bytesWritten + " total : " +
/// contentLength);
//                                    }
//                                }))
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
                .retryOnConnectionFailure(false)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit build = new Retrofit.Builder().client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new Retrofit2ConverterFactory())
//                .baseUrl(HttpProtocol.URL)
                .baseUrl("https://api.uzhixin.com")
                .build();
        ApiService apiService = build.create(ApiService.class);
        return apiService;
    }


    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        mRxSubscriber.dispose();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
