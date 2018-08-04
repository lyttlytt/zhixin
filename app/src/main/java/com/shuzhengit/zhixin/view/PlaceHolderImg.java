package com.shuzhengit.zhixin.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.library.util.DeviceUtil;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.util.ResourceUtils;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/1 19:05
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class PlaceHolderImg extends GlideDrawable {
    private Drawable mDrawable;

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public void setLoopCount(int loopCount) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Bitmap bitmap;
        if (mDrawable!=null){
            bitmap =  ((BitmapDrawable)mDrawable).getBitmap();
        }else {
            Drawable drawable = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable.big_placeholder_img);
             bitmap =  ((BitmapDrawable)drawable).getBitmap();
        }
        int screenWidth = DeviceUtil.getScreenWidth(APP.getInstance());
        screenWidth =screenWidth-60-bitmap.getWidth();
        canvas.drawBitmap(bitmap,screenWidth/2,0,null);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return mDrawable.getOpacity();
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
