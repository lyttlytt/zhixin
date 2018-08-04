package com.shuzhengit.zhixin;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.shuzhengit.zhixin.service.LocationService;
import com.shuzhengit.zhixin.util.CrashHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/20 16:06
 * E-mail:yuancongbin@gmail.com
 */

public class APP extends Application  {
    private static APP sInstance;
    private Vibrator mVibrator;
    public LocationService mLocationService;
    //只用于利用淘宝的地址获取用户手机外网ip地址
    public static final String USER_AGENT="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";
    public static final String SHARE_URL="http://www.uzhixin.com/";
    static {
        PlatformConfig.setWeixin("wxb782941cace8cb4b", "31014ba568c5b9619eef265afcbea930");
        PlatformConfig.setQQZone("101414611", "1605c5c0316409aab1df3d70aff3a952");
        PlatformConfig.setSinaWeibo("2237780642", "022e96532c0189fc7418a230bb08bbe0", "https://api.weibo.com/oauth2/default.html");        Config.DEBUG= true;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG= false;
        UMShareAPI.get(this);
        sInstance = this;
        //取消页面自动统计
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode( false );
        //缓存
        initCachePath();
        //crash统计
        CrashHandler instance = CrashHandler.getInstance();
        instance.init(this);
//        File file = new File(getCacheDir().getAbsoluteFile() + "/zhixin");
//        sCacheManager = CacheManager.get(file);
//        Hawk.init(this)
//                .setLogInterceptor(new LogInterceptor() {
//                    @Override
//                    public void onLog(String message) {
//                        LogUtils.i("hawk : " + message);
//                    }
//                }).build();
        /***
         * 初始化定位sdk，建议在Application中创建
         */

        mLocationService = new LocationService(this);
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
//        //检查内存泄露
//        if (LeakCanary.isInAnalyzerProcess(this))
//            return;
//        LeakCanary.install(this);
    }


    public static void initCachePath(){
//        //缓存
//        File file = new File(sInstance.getCacheDir().getAbsoluteFile() + "/zhixin");
//        if (!file.exists()){
//            file.mkdirs();
//        }
//        LogUtils.i(" cache path : " +file.getAbsolutePath());
//        sCacheManager = CacheManager.get(file);
    }
    public static APP getInstance() {
        return sInstance;
    }

}
