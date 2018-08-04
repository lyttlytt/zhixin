package com.shuzhengit.zhixin.question.release_question;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.library.base.BaseFragment;
import com.library.download.UploadFileRequestBody;
import com.library.download.UploadListener;
import com.library.permission.AcpListener;
import com.library.permission.AcpOptions;
import com.library.permission.AcpUtils;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.library.util.Retrofit2ConverterFactory;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.UploadImg;
import com.shuzhengit.zhixin.http.ApiService;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.BitmapUtils;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 14:12
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ReleaseQuestionFragment extends BaseFragment {
    private static final String TAG = "ReleaseQuestionFragment";
    TextView mTvTitle;
    TextView mTvRight;
    ImageView mIvRightShare;
    List<Uri> mSelected = new ArrayList<>();
    @BindView(R.id.etQuestionTitle)
    EditText mEtQuestionTitle;
    @BindView(R.id.etQuestionDetail)
    EditText mEtQuestionDetail;
    @BindView(R.id.ivAddImg)
    ImageView mIvAddImg;
    @BindView(R.id.llUpLoadImg)
    LinearLayout mLlUpLoadImg;
    private List<String> mUpLoadImage = new ArrayList<>();
    private ReleaseQuestionActivity mReleaseQuestionActivity;
    private int mMemberId;
    private Unbinder mUnbinder;

    public List<String> getUpLoadImage() {
        return mUpLoadImage;
    }

    public String getTitle() {
        return mEtQuestionTitle.getText().toString();
    }

    public String getDescription() {
        return mEtQuestionDetail.getText().toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_release_question;
    }

    public static ReleaseQuestionFragment getInstance(int memberId) {
        Bundle bundle = new Bundle();
        bundle.putInt("memberId", memberId);
        ReleaseQuestionFragment releaseQuestionFragment = new ReleaseQuestionFragment();
        releaseQuestionFragment.setArguments(bundle);
        return releaseQuestionFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        mMemberId = getArguments().getInt("memberId");
        mReleaseQuestionActivity = (ReleaseQuestionActivity) mActivity;
        mIvAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcpUtils.getInstance(APP.getInstance())
                        .request(new AcpOptions.Builder().setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
                                LogUtils.i(TAG, "go");
                                Matisse.from(ReleaseQuestionFragment.this)
                                        .choose(MimeType.allOf())
                                        .countable(true)
                                        .gridExpectedSize(360)
                                        .maxSelectable(3 - mSelected.size())
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
    }

    @Override
    protected void createPresenter() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10000) {
            mSelected = Matisse.obtainResult(data);
            for (int i = 0; i < mSelected.size(); i++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_upload_img,
                        mLlUpLoadImg, false);
                mLlUpLoadImg.addView(view, mLlUpLoadImg.getChildCount() - 1);
                if (mLlUpLoadImg.getChildCount() >= 4) {
                    mIvAddImg.setVisibility(View.GONE);
                } else {
                    mIvAddImg.setVisibility(View.VISIBLE);
                }

                Bitmap bitmap = BitmapUtils.decodeUri(APP.getInstance(), mSelected.get(i));
                byte[] bytes = BitmapUtils.compressImageToByte(bitmap);
                bitmap.recycle();
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
                UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(requestBody, new UploadListener() {
                    @Override
                    public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                        LogUtils.e("upload image  current : " + bytesWritten + " total : " + contentLength);
                    }
                });

                ApiService apiService = initHttp();
                apiService.uploadImg(requestBody)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<UploadImg>() {
                            @Override
                            protected void _onNext(UploadImg uploadImg) {
                                mUpLoadImage.add(uploadImg.getUrl());
                                ImageView iv = (ImageView) view.findViewById(R.id.iv);
                                GlideLoadImageUtils.loadImg(mReleaseQuestionActivity,uploadImg.getUrl(), iv);
                                ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
                                ivDelete.setVisibility(View.VISIBLE);
                                ivDelete.setTag(uploadImg.getUrl());
                                ivDelete.setOnClickListener(v -> {
                                    mIvAddImg.setVisibility(View.VISIBLE);
                                    String tag = (String) v.getTag();
                                    mUpLoadImage.remove(tag);
                                    ViewParent parent = v.getParent();
                                    if (parent instanceof FrameLayout) {
                                        mLlUpLoadImg.removeView((View) parent);
                                    }
                                });
                            }
                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
//                                    LogUtils.i(TAG, message);
                                dispose();
                            }
                        });
//
//                HttpProtocol.getApi()
//                        .uploadImg(requestBody)
//                        .compose(RxSchedulersHelper.io_main())
//                        .compose(RxResultHelper.handleResult())
//                        .subscribeWith(new RxSubscriber<UploadImg>() {
//                            @Override
//                            protected void _onNext(UploadImg uploadImg) {
//                                LogUtils.i(TAG, "upload image: " + uploadImg.getUrl());
//                                mUpLoadImage.add(uploadImg.getUrl());
//                                ImageView iv = (ImageView) view.findViewById(R.id.iv);
//                                GlideLoadImageUtils.loadImg(mReleaseQuestionActivity,uploadImg.getUrl(), iv);
//                                ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
//                                ivDelete.setVisibility(View.VISIBLE);
//                                ivDelete.setTag(uploadImg.getUrl());
//                                ivDelete.setOnClickListener(v -> {
//                                    mIvAddImg.setVisibility(View.VISIBLE);
//                                    String tag = (String) v.getTag();
//                                    mUpLoadImage.remove(tag);
//                                    ViewParent parent = v.getParent();
//                                    if (parent instanceof FrameLayout) {
//                                        mLlUpLoadImg.removeView((View) parent);
//                                    }
//                                });
//                            }
//
//                            @Override
//                            protected void _onError(String message) {
//                                super._onError(message);
////                                    LogUtils.i(TAG, message);
//                                dispose();
//                            }
//                        });

//                InputStream inputStream = null;
//                try {
//                    inputStream = mActivity.getContentResolver().openInputStream(mSelected.get(i));
////                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    Bitmap bitmap = BitmapUtils.decodeUri(APP.getInstance(), mSelected.get(i));
//                    byte[] bytes = BitmapUtils.compressImageToByte(bitmap);
//                    bitmap.recycle();
//                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
//                    HttpProtocol.getApi()
//                            .uploadImg(requestBody)
//                            .compose(RxSchedulersHelper.io_main())
//                            .compose(RxResultHelper.handleResult())
//                            .subscribeWith(new RxSubscriber<UploadImg>() {
//                                @Override
//                                protected void _onNext(UploadImg uploadImg) {
//                                    LogUtils.i(TAG, "upload image: " + uploadImg.getUrl());
//                                    mUpLoadImage.add(uploadImg.getUrl());
//                                }
//
//                                @Override
//                                protected void _onError(String message) {
//                                    super._onError(message);
////                                    LogUtils.i(TAG, message);
//                                    dispose();
//                                }
//                            });
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    private ApiService initHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .method(original.method(), new UploadFileRequestBody(original.body(), new UploadListener() {
                                    @Override
                                    public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                                        LogUtils.e("upload image  current : " + bytesWritten + " total : " + contentLength);
                                    }
                                }))
                                .build();
                        return chain.proceed(request);
                    }
                })
                .retryOnConnectionFailure(false)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit build = new Retrofit.Builder().client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new Retrofit2ConverterFactory())
                .baseUrl(HttpProtocol.URL)
                .build();
        ApiService apiService = build.create(ApiService.class);
        return apiService;
    }


    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
