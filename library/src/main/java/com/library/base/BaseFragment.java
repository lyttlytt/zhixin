package com.library.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.library.util.DeviceUtil;
import com.library.util.ToastUtils;


/**
 * Created by 袁从斌-where on 2016/12/19.
 * 所有fragment的基类
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements
        BaseView<P> {
    @Override
    public void failure(int resId) {
        String string = getString(resId);
        failure(string);
    }

    protected BaseActivity mActivity;
    private View mView;

    //    获取fragment布局文件的ID

    /**
     * get fragment layout resource id
     */
    protected abstract int getLayoutId();

    /**
     * presenter基类
     */
    protected P mBasePresenter;

    // get activity
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    protected DefaultLoadingDialogFragment mDefaultLoadingDialogFragment = null;

    /**
     * initialization content view
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * initialization presenter
     */
    protected abstract void createPresenter();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Override
    public void setPresenter(P presenter) {
        if (null == presenter) {
            throw new NullPointerException("presenter can not be null ");
        }
        mBasePresenter = presenter;
    }
    //添加fragment
//    protected void addFragment(BaseFragment fragment){
//        if (null!=fragment){
//            getHoldingActivity().addFragment(fragment);
//        }
//    }
//    protected void removeFragment(){
//        getHoldingActivity().removeFragment();
//    }

    @Override
    public void requestDataOnError(String message) {
        ToastUtils.showShortToast(getContext().getApplicationContext(), message);
    }

    //    @Override
//    public void failure(View view, String msg) {
//        showSnackbarShort(msg);
//    }
    @Override
    public void failure(String msg) {
        ToastUtils.showShortToast(getContext().getApplicationContext(), msg);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        initView(mView, savedInstanceState);
        createPresenter();
        return mView;
    }


    /**
     * show loading dialog
     * in fragment use childFragmentManager
     */
    private void showLoadingDialog() {
        mDefaultLoadingDialogFragment = new DefaultLoadingDialogFragment();
        mDefaultLoadingDialogFragment.show(getChildFragmentManager(), "loadingDialog");
    }

    /**
     * hide loading dialog
     */
    private void hideLoadingDialog() {
        if (mDefaultLoadingDialogFragment != null) {
            mDefaultLoadingDialogFragment.dismiss();
            mDefaultLoadingDialogFragment = null;
        }
    }

//    /**
//     * show snackBar duration is short
//     * {@link SnackbarUtils}
//     */
//    protected void showSnackbarShort(String message) {
//        SnackbarUtils.showSnackbarShort(mView, message);
//    }

//    /**
//     * show snackBar duration is long
//     * {@link SnackbarUtils}
//     */
//    protected void showSnackbarLong(String message) {
//        SnackbarUtils.showSnackbarLong(mView, message);
//    }

//    /**
//     * show snackBar duration is indefinite
//     * {@link SnackbarUtils}
//     */
//    protected void showSnackbarIndefinite(String message) {
//        SnackbarUtils.showSnackbarIndefinite(mView, message);
//    }

    @Override
    public void onDestroy() {
        if (mDefaultLoadingDialogFragment != null) {
//            mDefaultLoadingDialogFragment.getDialog().dismiss();
            mDefaultLoadingDialogFragment.dismiss();
            mDefaultLoadingDialogFragment = null;
        }
        if (mBasePresenter != null) {
            mBasePresenter.release();
        }
        super.onDestroy();
    }


    /**
     * get systemBar height
     *
     * @return
     */
    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            String status_bar_height = clazz.getField("status_bar_height").get(obj).toString();
            int i = Integer.parseInt(status_bar_height);
            //dp2px
            statusBarHeight = DeviceUtil.px2dp(getContext().getApplicationContext(), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 跳转activity
     * @param targetClass 目标 activity
     * @see #startActivity(Class, Bundle)
     */
    public void startActivity(Class<?> targetClass) {
        startActivity(targetClass, null);
    }

    /**
     * 跳转activity
     *
     * @param targetClass 目标 activity
     * @param bundle      数据
     */
    public void startActivity(Class<?> targetClass, Bundle bundle) {
        Intent intent = new Intent(getHoldingActivity(), targetClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转activity并finish当前的activity
     *
     * @param targetClass 目标activity
     * @see #startActivity(Class, Bundle)
     */
    public void startActivityAndKill(Class<?> targetClass) {
        startActivity(targetClass, null);
        getHoldingActivity().finish();
    }

    /**
     * 跳转activity并finish当前的activity
     *
     * @param targetClass 目标activity
     * @see #startActivity(Class, Bundle)
     */
    public void startActivityAndKill(Class<?> targetClass, Bundle bundle) {
        startActivity(targetClass, bundle);
        getHoldingActivity().finish();
    }
}
