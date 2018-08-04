package com.shuzhengit.zhixin.question.answer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.rx.RxBus2;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.DocumentPicture;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.UploadImg;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.BitmapUtils;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.KeyBorderLeakUtil;
import com.shuzhengit.zhixin.view.PictureAndTextEditorView;
import com.umeng.analytics.MobclickAgent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddAnswerActivity extends BaseActivity<AddAnswerContract.Presenter> implements AddAnswerContract.View {
    private static final String TAG = "AddAnswerActivity";
    List<Uri> mSelected = new ArrayList<>();
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.picAndTextEditor)
    PictureAndTextEditorView mPicAndTextEditor;
    @BindView(R.id.ivInsertImg)
    ImageView mIvInsertImg;
    private Unbinder mUnbinder;
    private int mQuestionId;
    private int mMemberId;

    @Override
    protected int layoutId() {
        return R.layout.activity_add_answer;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mQuestionId = getIntent().getIntExtra("questionId", -1);
        mMemberId = getIntent().getIntExtra("memberId", -1);
        if (mQuestionId == -1) {
            failure("该问题非法,禁止回答");
            onBackPressed();
        }
        if (mMemberId == -1) {
            failure("请先登录");
            onBackPressed();
        }

        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvRight.setVisibility(View.VISIBLE);
        mTvTitle.setText("添加回答");
        mTvRight.setText("完成");
        mTvRight.setOnClickListener(v -> {
            failure("完成");
            LogUtils.i(mPicAndTextEditor.getmContentList().toString());
            LogUtils.i(mPicAndTextEditor.getText().toString());
            String s = mPicAndTextEditor.getText().toString();
            String regExImg = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern compile = Pattern.compile(regExImg, Pattern.CASE_INSENSITIVE);
            Matcher matcher = compile.matcher(s);
            List<DocumentPicture> imgs = new ArrayList<>();
            while (matcher.find()) {
                //得到<img />标签
                String group = matcher.group();
                DocumentPicture documentPicture = new DocumentPicture();
                documentPicture.setSrc(group);
                documentPicture.setAlt("");
                documentPicture.setPixel("");
                documentPicture.setRef("");
                imgs.add(documentPicture);
            }
            String replace = s;
            for (int i = 0; i < imgs.size(); i++) {
                replace = s.replace(imgs.get(i).getSrc(), "<!--IMG#" + i + "-->");
            }
            LogUtils.i(TAG, "替换后 : " + replace);
            mBasePresenter.postAnswer(replace, mQuestionId, mMemberId, imgs);
        });

        mIvInsertImg.setOnClickListener(v -> {
            Matisse.from(AddAnswerActivity.this)
                    .choose(MimeType.allOf())
                    .countable(true)
                    .gridExpectedSize(360)
                    .maxSelectable(3)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(10000);
        });
        mToolBar.setNavigationOnClickListener(v -> {
            RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.RELEASE_REFRESH_QUESTION_ANSWER,""));
            onBackPressed();
        });
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new AddAnswerPresenter(this);
    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            KeyBorderLeakUtil.fixFocusedViewLeak(APP.getInstance());
        }
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10000) {
            mSelected = Matisse.obtainResult(data);
            for (Uri uri : mSelected) {
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(mSelected.get(0));
                    Bitmap bitmap = BitmapUtils.decodeUri(APP.getInstance(), mSelected.get(0));
                    byte[] bytes = BitmapUtils.compressImageToByte(bitmap);
                    LogUtils.e(TAG,"bytes : "+bytes.length);
                    bitmap.recycle();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
                    HttpProtocol.getApi()
                            .uploadImg(requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .compose(RxResultHelper.handleResult())
                            .subscribeWith(new RxSubscriber<UploadImg>() {
                                @Override
                                protected void _onNext(UploadImg uploadImg) {
                                    LogUtils.i(TAG, "upload image: " + uploadImg.getUrl());
                                    mPicAndTextEditor.insertBitmap(uploadImg.getUrl(), uri);
                                }

                                @Override
                                protected void _onError(String message) {
                                    super._onError(message);
                                    LogUtils.i(TAG, message);
                                    dispose();
                                }
                            });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
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
    public void refreshAnswerList() {
        RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.REFRESH_ANSWER, "refreshAnswer"));
        onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.RELEASE_REFRESH_QUESTION_ANSWER,""));
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
