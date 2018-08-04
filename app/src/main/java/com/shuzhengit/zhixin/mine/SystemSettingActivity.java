package com.shuzhengit.zhixin.mine;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.permission.AcpListener;
import com.library.permission.AcpOptions;
import com.library.permission.AcpUtils;
import com.library.rx.RxSchedulersHelper;
import com.library.util.AppManager;
import com.library.util.DataCleanManager;
import com.library.util.DeviceUtil;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.HomeActivity;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.CheckVersion;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.http.download.RxDownLoadService;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CacheUtils;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.DialogUtil;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SystemSettingActivity extends BaseActivity {
    private static final String TAG = "SystemSettingActivity";

//    TextView mTvTitle;
//    TextView mTvRight;
//    ImageView mIvRightShare;
//    Toolbar mToolBar;
//    TextView mTvItemName;
//    TextView mTvCacheSize;
//    RelativeLayout mRlClearCache;
    @BindView(R.id.unLogin)
    Button mUnLogin;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.tvItemName)
    TextView mTvItemName;
    @BindView(R.id.tvCacheSize)
    TextView mTvCacheSize;
    @BindView(R.id.rlClearCache)
    RelativeLayout mRlClearCache;
    @BindView(R.id.tvCheckUpdate)
    TextView mTvCheckUpdate;
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
        return R.layout.activity_system_setting;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
//        mTvTitle = (TextView) findViewById(R.id.tvTitle);
//        mTvRight = (TextView) findViewById(R.id.tvRight);
//        mIvRightShare = (ImageView) findViewById(R.id.ivRightShare);
//        mToolBar = (Toolbar) findViewById(R.id.toolBar);
//        mTvItemName = (TextView) findViewById(R.id.tvItemName);
//        mTvCacheSize = (TextView) findViewById(R.id.tvCacheSize);
//        mRlClearCache = (RelativeLayout) findViewById(R.id.rlClearCache);


        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        mTvTitle.setText("系统设置");
        String cacheDir = getCacheDir().getPath();
        long cacheSize = getCacheSize(cacheDir);
        if (DeviceUtil.isSDCardAvailable()) {
            File externalCacheDir = getExternalCacheDir();
            if (externalCacheDir != null) {
                cacheSize += getCacheSize(externalCacheDir.getPath());
            }
        }
        User user = CheckUser.checkUserIsExists();
        if (user==null){
            mUnLogin.setVisibility(View.GONE);
        }else {
            mUnLogin.setVisibility(View.VISIBLE);
            mUnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unLogin();
                }
            });
        }

        mTvCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpProtocol.getApi().checkVersion(Integer.parseInt(DeviceUtil.getVersionCode(SystemSettingActivity
                        .this)))
                        .compose(RxSchedulersHelper.io_main())
                        .subscribeWith(new RxSubscriber<BaseCallModel<CheckVersion>>() {
                            @Override
                            protected void _onNext(BaseCallModel<CheckVersion> checkVersion) {
                                if (checkVersion.getData() != null) {
                                    showUpdateDialog(checkVersion.getData());
                                }else {
                                    failure("已经是最新的版本了");
                                }
                            }
                        });
            }
        });

        mTvCacheSize.setText(DataCleanManager.getFormatSize(cacheSize));
        mRlClearCache.setOnClickListener(v -> {
            DialogUtil.showDialog(this, "提示", "真的的要清除缓存吗?", true, new DialogUtil.DialogCallBack() {
                @Override
                public void callBackNegative(DialogInterface dialog) {

                }

                @Override
                public void callBackPositive(DialogInterface dialog) {
                    AcpUtils.getInstance(SystemSettingActivity.this)
                            .request(new AcpOptions.Builder().setPermissions(Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
                                @Override
                                public void onGranted() {
//                                    String cacheDir = getCacheDir().getPath();
//                                    if (DeviceUtil.isSDCardAvailable()) {
//                                        File externalCacheDir = getExternalCacheDir();
//                                        if (externalCacheDir!=null) {
//                                            DataCleanManager.cleanExternalCache(SystemSettingActivity.this);
//                                        }
//                                    }
////                                    DataCleanManager.deleteFolderFile(getCacheDir().getPath(), false);
//                                    long cacheSize = getCacheSize(cacheDir);
//                                    if (DeviceUtil.isSDCardAvailable()){
//                                        File externalCacheDir = getExternalCacheDir();
//                                        if (externalCacheDir!=null) {
//                                            cacheSize += getCacheSize(externalCacheDir.getPath());
//                                        }
//                                    }
//                                    mTvCacheSize.setText(DataCleanManager.getFormatSize(cacheSize));
//                                    CacheUtils.getCacheManager(APP.getInstance()).clear();
//                                    CacheManager cacheManager = CacheUtils.getCacheManager(SystemSettingActivity
// .this);
//                                    cacheManager = null;
//                                    CacheUtils.getCacheManager(APP.getInstance());
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            Glide.get(APP.getInstance()).clearDiskCache();
                                        }
                                    }.start();
//                                    AppManager.finishActivity(HomeActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putInt(EventCodeUtils.REFRESH_USER, 1);
//                                    startActivity(HomeActivity.class, bundle);
                                    onBackPressed();

                                }

                                @Override
                                public void onDenied(List<String> permissions) {

                                }
                            });
                }
            });
        });
    }

    private long getCacheSize(String path) {
        long cacheSize = 0;
        File file = new File(path);
        try {
            cacheSize = DataCleanManager.getFolderSize(file);
        } catch (Exception e) {
            e.printStackTrace();
            failure("无法获取缓存目录");
        }
        return cacheSize;
    }

    @Override
    protected void createPresenter() {

    }

    public void unLogin() {
        DialogUtil.showDialog(this, "提示", "真的要退出登录吗,一些功能将会受到限制哦!", false, new DialogUtil.DialogCallBack() {
            @Override
            public void callBackNegative(DialogInterface dialog) {

            }

            @Override
            public void callBackPositive(DialogInterface dialog) {
                CacheUtils.getCacheManager(SystemSettingActivity.this).remove("user");
                CacheUtils.getCacheManager(SystemSettingActivity.this).remove("token");
                AppManager.finishActivity(HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EventCodeUtils.REFRESH_USER, 1);
                startActivity(HomeActivity.class, bundle);

//                RxBus2.getDefault().post(new EventType(EventCodeUtils.REFRESH_USER,
//                        "refresh"));
            }
        });
    }
    private void showUpdateDialog(CheckVersion checkVersion) {
        AcpUtils.getInstance(this).request(new AcpOptions.Builder().setPermissions(new String[]{Manifest.permission
                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}).build(), new AcpListener() {
            @Override
            public void onGranted() {
                new AlertDialog.Builder(SystemSettingActivity.this)
                        .setTitle("版本更新")
                        .setMessage(checkVersion.getDescription())
                        .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtils.e(TAG,"update : " + Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Test");
                                Intent intent = new Intent(SystemSettingActivity.this, RxDownLoadService.class);
                                intent.putExtra("downloadUrl",checkVersion.getDownloadUri());
                                startService(intent);
                            }
                        })
                        .show();
            }

            @Override
            public void onDenied(List<String> permissions) {

            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
