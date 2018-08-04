package com.library.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.library.statusbar.StatusBarUtil;
import com.library.util.AppManager;
import com.library.util.DeviceUtil;
import com.library.util.ToastUtils;


/**
 * Created by 袁从斌-where on 2016/12/19.
 * activity的基类
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements
        BaseView<P> {

    protected DialogFragment defaultLoadingDialogFragment;
    private boolean isTranslucentStatusBar = false;
    public P mBasePresenter;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        mContext = this;
        initView();
        if (isTranslucentStatusBar) {
            StatusBarUtil.setStatusTranslucent(this);
        } else {
            StatusBarUtil.setStatusBarColor(this);
        }
        setStatusBarColor();
        createPresenter();
        //添加视图查看器
//        ViewServer.get(this).addWindow(this);
        //添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
    }
    public void setStatusBarColor(){}
    public void setDefaultLoadingDialogFragment(DialogFragment defaultLoadingDialogFragment) {
        this.defaultLoadingDialogFragment = defaultLoadingDialogFragment;
    }

    /**
     * setContentView(layoutId);
     * @return layoutId
     */
    protected abstract int layoutId();

    @Override
    public void failure(int resId) {
        String string = getString(resId);
        failure(string);
    }

    @Override
    public void setPresenter(P presenter) {
        mBasePresenter = presenter;
    }

    /**
     * if you set image translucentStatusBar
     * please set isTranslucentStatusBar == true
     *
     * @param isTranslucentStatusBar default false
     *     *  @see #setStatusBarColor()
     *     *  @see #setStatusTranslucent()
     */
    protected void setTranslucentStatusBar(boolean isTranslucentStatusBar) {
        this.isTranslucentStatusBar = isTranslucentStatusBar;
    }

    @Override
    public void requestDataOnError(String message) {
        ToastUtils.showShortToast(getApplicationContext(), message);
    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            String status_bar_height = clazz.getField("status_bar_height").get(obj).toString();
            int i = Integer.parseInt(status_bar_height);
            //dp2px
            statusBarHeight = DeviceUtil.px2dp(this.getApplicationContext(), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    @Override
    public void failure(String msg) {
        ToastUtils.showShortToast(this.getApplicationContext(),msg);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    /**
     * initialization page
     */
    protected abstract void initView();

    /**
     * initialization presenter
     */
    protected abstract void createPresenter();

    @Override
    protected void onResume() {
        super.onResume();
        //设置视图焦点
//        ViewServer.get(this).setFocusedWindow(this);
    }

    /**
     * show loading dialog
     * {@link DefaultLoadingDialogFragment}
     */
    protected void showLoadingDialog() {
        defaultLoadingDialogFragment = new DefaultLoadingDialogFragment();
        defaultLoadingDialogFragment.show(getSupportFragmentManager(), "loadingDialog");
    }

    /**
     * hide Loading Dialog
     * {@link DefaultLoadingDialogFragment}
     */
    protected void hideLoadingDialog() {
        if (defaultLoadingDialogFragment != null) {
            defaultLoadingDialogFragment.dismiss();
            defaultLoadingDialogFragment=null;
        }
    }

    /**
     * 跳转activity
     * @see #startActivity(Class,Bundle)
     * @param targetClass 目标 activity
     */
    public void startActivity(Class<?> targetClass){
        startActivity(targetClass,null);
    }

    /**
     *  跳转activity
     * @param targetClass 目标 activity
     * @param bundle 数据
     */
    public void startActivity(Class<?> targetClass,Bundle bundle){
        Intent intent = new Intent(this, targetClass);
        if (bundle!=null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转activity并finish当前的activity
     * @see #startActivity(Class, Bundle)
     * @param targetClass 目标activity
     */
    public void startActivityAndKill(Class<?> targetClass){
        startActivity(targetClass,null);
        finish();
    }
    /**
     * 跳转activity并finish当前的activity
     * @see #startActivity(Class, Bundle)
     * @param targetClass 目标activity
     */
    public void startActivityAndKill(Class<?> targetClass,Bundle bundle){
        startActivity(targetClass,bundle);
        finish();
    }

    /**
     * 跳转activity 并等待回传的数据
     * @see #startActivityForResult(Class, Bundle,int)
     * @param targetClass 目标activity
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> targetClass,int requestCode){
        startActivityForResult(targetClass,null,requestCode);
    }

    /**
     * 跳转activity 并等待回传的数据
     * @param targetClass 目标activity
     * @param bundle 数据
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> targetClass,Bundle bundle,int requestCode){
        Intent intent = new Intent(this, targetClass);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (defaultLoadingDialogFragment!=null){
            defaultLoadingDialogFragment.dismiss();
            defaultLoadingDialogFragment=null;
        }
        if (mBasePresenter != null) {
            mBasePresenter.release();
            mBasePresenter = null;
        }
        //结束activity从堆栈中移除
        AppManager.getAppManager().removeActivity(this);
        //把视图查看器移除
//        ViewServer.get(this).removeWindow(this);
        super.onDestroy();
    }

}
