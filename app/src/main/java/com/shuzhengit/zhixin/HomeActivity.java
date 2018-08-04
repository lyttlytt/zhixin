package com.shuzhengit.zhixin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.permission.AcpListener;
import com.library.permission.AcpOptions;
import com.library.permission.AcpUtils;
import com.library.rx.RxSchedulersHelper;
import com.library.util.AppManager;
import com.library.util.DeviceUtil;
import com.library.util.LogUtils;
import com.library.util.SPUtils;
import com.shuzhengit.zhixin.bean.CheckVersion;
import com.shuzhengit.zhixin.bean.IPModel;
import com.shuzhengit.zhixin.bean.Token;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.http.download.RxDownLoadService;
import com.shuzhengit.zhixin.index.ZhiXinFragment;
import com.shuzhengit.zhixin.login.LoginModel;
import com.shuzhengit.zhixin.mine.MineFragment;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.service.LocationService;
import com.shuzhengit.zhixin.sign_in.money.MoneyActivity;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CacheUtils;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.wenba.WenBaFragment;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    ViewPager mHomeViewPager;
    TabLayout mHomeTabLayout;
    //    private int[] mHomeTabDrawables = new int[]{R.drawable.selector_zx, R.drawable.selector_discover, R.drawable
//            .selector_qeustion, R.drawable.selector_mine};
    private int[] mHomeTabDrawables = new int[]{R.drawable.selector_zx, R.drawable
            .selector_qeustion, R.drawable.selector_mine};
    private LocationService mLocationService;
    private boolean mShowSplash;
    private SplashFragment mSplashFragment;
    private int mIntExtra;
    private int panduan2;
    @Override
    protected void onStop() {
        if (mLocationService != null) {
            mLocationService.unregisterListener(mBDLocationListener); //注销掉监听
            mLocationService.stop(); //停止定位服务
        }
        super.onStop();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_home;


    }

    @Override
    protected void initView() {
//红包效果
        hongbao hb= new hongbao(this);
        hb.show();

        String versionCode = DeviceUtil.getVersionCode(this);
        String versionName = DeviceUtil.getVersionName(this);
        mShowSplash = (boolean) SPUtils.get(this, CacheKeyManager.SHOW_SPLASH, true);
        mIntExtra = getIntent().getIntExtra(EventCodeUtils.REFRESH_USER, -1);
        if (mShowSplash && mIntExtra == -1) {
            mSplashFragment = new SplashFragment();
            mSplashFragment.show(getFragmentManager(), "splash");
        }
        mHomeViewPager = (ViewPager) findViewById(R.id.homeViewPager);
        mHomeTabLayout = (TabLayout) findViewById(R.id.homeTableLayout);
        AcpUtils.getInstance(APP.getInstance())
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission_group.STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE).build()
                        , new AcpListener() {
                            @Override
                            public void onGranted() {

                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                            }
                        });


        ZhiXinFragment zhiXinFragment = new ZhiXinFragment();
//        DiscoverFragment discoverFragment = new DiscoverFragment();
//        QuestionFragment questionFragment = new QuestionFragment();
        WenBaFragment questionFragment = new WenBaFragment();
        MineFragment mineFragment = new MineFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(zhiXinFragment);
        fragments.add(questionFragment);
        fragments.add(mineFragment);
        String[] stringArray = getResources().getStringArray(R.array.homeTabNames);
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragments) {
            @Override
            public CharSequence getPageTitle(int position) {
                return stringArray[position];
            }
        };
        mHomeViewPager.setAdapter(adapter);
        mHomeViewPager.setOffscreenPageLimit(fragments.size());
        mHomeTabLayout.setupWithViewPager(mHomeViewPager);
        for (int i = 0; i < mHomeTabDrawables.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_home_tab, mHomeTabLayout, false);
            ((ImageView) view.findViewById(R.id.ivTab)).setImageResource(mHomeTabDrawables[i]);
            ((TextView) view.findViewById(R.id.tvTabName)).setText(stringArray[i]);
            TabLayout.Tab tab = mHomeTabLayout.getTabAt(i);
            if (null != tab) {
                tab.setCustomView(view);
            }
        }
        if (mIntExtra == 1) {
            mHomeViewPager.setCurrentItem(0);
        }
        AcpUtils.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission
                .LOCATION_HARDWARE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                .ACCESS_FINE_LOCATION).build(), new AcpListener() {
            @Override
            public void onGranted() {
                // -----------location config ------------
                mLocationService = APP.getInstance().mLocationService;
                //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
                mLocationService.registerListener(mBDLocationListener);
                LocationClientOption defaultLocationClientOption = mLocationService.getDefaultLocationClientOption();
                defaultLocationClientOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
                defaultLocationClientOption.SetIgnoreCacheException(true);
                defaultLocationClientOption.setOpenGps(true);
                mLocationService.setLocationOption(defaultLocationClientOption);
                mLocationService.start();
            }

            @Override
            public void onDenied(List<String> permissions) {
            }
        });
        mHomeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (tab.getPosition() == 0) {
                    if (zhiXinFragment!=null) {
                        zhiXinFragment.startSmoothTop();
                    }
                }
            }
        });
//        new Thread(){
//            @Override
//            public void run() {
//                String netIp = DeviceUtil.getNetIp();
//            }
//        }.start();


//        String deviceId = DeviceUtil.getDeviceId(HomeActivity.this);
//        LogUtils.d(TAG,deviceId);
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int densityDpi = metrics.densityDpi;
//        LogUtils.i("phone dpi : " + densityDpi);
//        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.logo);
//        LogUtils.i("phone dpi : " + densityDpi + " drawable size : " + drawable.getIntrinsicWidth() + "*" + drawable
//                .getIntrinsicHeight());
//        LogUtils.i("phone dpi : " + DeviceUtil.getScreenWidth(this));
    }


    @Override
    protected void createPresenter() {
        if (mShowSplash && mIntExtra == -1) {
            HttpProtocol.getApi().checkVersion(Integer.parseInt(DeviceUtil.getVersionCode(this)))
                    .compose(RxSchedulersHelper.io_main())
                    .subscribeWith(new RxSubscriber<BaseCallModel<CheckVersion>>() {
                        @Override
                        protected void _onNext(BaseCallModel<CheckVersion> checkVersion) {
                            if (checkVersion.getData() != null) {
                                showUpdateDialog(checkVersion.getData());
                            }
                        }
                    });
        }
        if (mShowSplash && mIntExtra == -1) {
            User user = CheckUser.checkUserIsExists();
            Token token = CheckUser.checkTokenIsExists();
            if (user != null && token != null) {
                HttpProtocol.getApi()
                        .refreshToken("refresh_token",
                                LoginModel.CLIENTID, LoginModel.CLIENTSECRET, token.getRefresh_token())
                        .compose(RxSchedulersHelper.io_main())
                        .subscribe(new DisposableSubscriber<Token>() {
                            @Override
                            public void onNext(Token token) {
                                CacheUtils.getCacheManager(HomeActivity.this).remove(CacheKeyManager.TOKEN);
                                CacheUtils.getCacheManager(HomeActivity.this).put(CacheKeyManager.TOKEN, token);

                            }

                            @Override
                            public void onError(Throwable t) {
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                Flowable.create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(FlowableEmitter<String> e) throws Exception {
                        HttpProtocol.getApi()
                                .getIP(APP.USER_AGENT)
                                .subscribeWith(new DisposableSubscriber<IPModel>() {
                                    @Override
                                    public void onNext(IPModel ipModel) {
                                        if (ipModel.getData() != null) {
                                            e.onNext(ipModel.getData().getIp());
                                        } else {
                                            e.onError(new Throwable("ip cannot be null"));
                                        }
                                        e.onComplete();
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        e.onError(t);
                                    }

                                    @Override
                                    public void onComplete() {
                                        e.onComplete();
                                    }
                                });
//                        String netIp = DeviceUtil.getNetIp();
//                        if (TextUtils.isEmpty(netIp)) {
//                            e.onError(new Throwable("ip cannot be null"));
//                        } else {
//                            e.onNext(netIp);
//                        }
//                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                        .compose(RxSchedulersHelper.io_main())
                        .flatMap(new Function<String, Publisher<RequestBody>>() {
                            @Override
                            public Publisher<RequestBody> apply(String s) throws Exception {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("lastLoginIp", s);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format = sdf.format(System.currentTimeMillis());
                                jsonObject.put("lastLoginTime", format);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; " +
                                        "charset=utf-8"), jsonObject
                                        .toString());
                                return Flowable.create(new FlowableOnSubscribe<RequestBody>() {
                                    @Override
                                    public void subscribe(FlowableEmitter<RequestBody> e) throws Exception {
                                        e.onNext(requestBody);
                                        e.onComplete();
                                    }
                                }, BackpressureStrategy.BUFFER);
                            }
                        })
                        .subscribeWith(new RxSubscriber<RequestBody>() {
                            @Override
                            protected void _onNext(RequestBody s) {
                                HttpProtocol.getApi()
                                        .postUserData(user.getId(), s)
                                        .compose(RxSchedulersHelper.io_main())
                                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
                                            @Override
                                            protected void _onNext(BaseCallModel callModel) {

                                            }
                                        });
                            }
                        });
//                String ipAddress = DeviceUtil.getIPAddress(this);
//                Log.i(TAG,"IP : " +);
//                HttpProtocol.getApi()
//                        .postUserData()
            }
        }
//        String deviceInfo = getDeviceInfo(this);
//        LogUtils.i("友盟信息:" + deviceInfo);
    }

    private void showUpdateDialog(CheckVersion checkVersion) {
        AcpUtils.getInstance(this).request(new AcpOptions.Builder().setPermissions(new String[]{Manifest.permission
                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}).build(), new AcpListener() {
            @Override
            public void onGranted() {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("版本更新")
                        .setMessage(checkVersion.getDescription())
                        .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtils.e(TAG, "update : " + Environment.getExternalStorageDirectory()
                                        .getAbsolutePath() + File.separator + "Test");
                                Intent intent = new Intent(HomeActivity.this, RxDownLoadService.class);
                                intent.putExtra("downloadUrl", checkVersion.getDownloadUri());
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

    private BDLocationListener mBDLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            SPUtils.put(HomeActivity.this, CacheKeyManager.BDProvince, bdLocation.getProvince());
            SPUtils.put(HomeActivity.this, CacheKeyManager.BDCity, bdLocation.getCity());
            SPUtils.put(HomeActivity.this, CacheKeyManager.BDCityCode, bdLocation.getCityCode());
//            LogUtils.i("baidu", bdLocation.getCity());
//            LogUtils.i("baidu", bdLocation.getProvince());
//            LogUtils.i("baidu", bdLocation.getCityCode());
//            LogUtils.i("baidu", bdLocation.getStreetNumber());
//            LogUtils.i("baidu", bdLocation.getCountryCode());
//            DialogUtil.showSingleButtonDialog(HomeActivity.this, bdLocation.getCity(), bdLocation.getCity(), false,
            mLocationService.stop();
            mLocationService.unregisterListener(mBDLocationListener);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            LogUtils.e("onConnectHotSpot : " + s + "============" + i);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //统计页面的时长
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //统计页面的时长
        MobclickAgent.onPause(this);
        if (mSplashFragment != null) {
            mSplashFragment.onHide();
            mSplashFragment = null;
        }
    }

    /**
     * 声明一个long类型变量：用于存放上一点击“返回键”的时刻
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                failure(R.string.clickBackKeyAgainExit);
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                SPUtils.put(this, CacheKeyManager.SHOW_SPLASH, true);
                //后台进程保活请不要反注册RxBus,要不然接收不到消息
//                RxBus2.getDefault().unRegister();
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                AppManager.AppExit(this, true);
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
