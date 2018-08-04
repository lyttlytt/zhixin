package com.shuzhengit.zhixin.index.document;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.library.base.BaseActivity;
import com.library.permission.AcpListener;
import com.library.permission.AcpOptions;
import com.library.permission.AcpUtils;
import com.library.util.DeviceUtil;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends BaseActivity {
    private static final String TAG = "PhotoViewActivity";
    private PhotoView mPhotoView;
    private TextView mTvSave;
    private ProgressBar mPb;

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
        return R.layout.activity_photo_view;
    }

    @Override
    protected void initView() {
        setTranslucentStatusBar(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        mTvSave = (TextView) findViewById(R.id.save);
        mPb = (ProgressBar) findViewById(R.id.pb);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mPhotoView = (PhotoView) findViewById(R.id.photoView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(0, DeviceUtil.getStatusBarHeight(this), 0, 0);
        toolbar.setLayoutParams(layoutParams);
        String serializableExtra = getIntent().getStringExtra(EventCodeUtils
                .DOCUMENT_PICTURE);
        Log.d(TAG,serializableExtra);
//        LogUtils.i(TAG,serializableExtra);
        Glide.with(this).load(serializableExtra)
//                .centerCrop()
                .dontAnimate()
                .priority(Priority.HIGH)
                .into(new GlideDrawableImageViewTarget(mPhotoView){
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                mPb.setVisibility(View.GONE);
                mPhotoView.setImageDrawable(resource);
            }
        });
//        GlideLoadImageUtils.loadImg(this, serializableExtra, mPhotoView);
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        AcpUtils.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission
                .WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                Bitmap bitmap = mPhotoView.getVisibleRectangleBitmap();
                String zhixin = DeviceUtil.createAPPFolder("zhixin", PhotoViewActivity.this.getApplication());
                File folder = new File(zhixin);
                if (!folder.exists()||!folder.isDirectory()){
                    folder.mkdirs();
                }
                File file = new File(zhixin, System.currentTimeMillis()+".png");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.flush();
                    //保存图片后发送广播通知更新数据库
                    Uri uri = Uri.fromFile(file);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    mTvSave.setVisibility(View.GONE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    bitmap.recycle();
                    bitmap=null;
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onDenied(List<String> permissions) {

            }
        });

    }

    @Override
    protected void createPresenter() {

    }
}
