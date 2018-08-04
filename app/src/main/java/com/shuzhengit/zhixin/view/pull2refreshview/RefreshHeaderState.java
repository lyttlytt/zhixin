package com.shuzhengit.zhixin.view.pull2refreshview;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/24 11:47
 * E-mail:yuancongbin@gmail.com
 */

public interface RefreshHeaderState {
    int STATE_NORMAL=0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING =2;
    int STATE_COMPLETED =3;
    void onMove(float delta);
    boolean releaseAction();
    void refreshCompleted();
}
