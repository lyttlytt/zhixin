package com.shuzhengit.zhixin.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by 袁从斌-where on 2017/2/9.
 * 使用glide加载图片
 */

public class GlideLoadImageUtils {
    private static final String ANDROID_RESOURCE="android.resource://";
    private static final String FORWARD_SLASH= File.separator;

    /**加载图片*/
    public static void loadImg(Context context,String url, ImageView iv){
        Glide.with(context)
                .load(url)
                .fitCenter()
                .into(iv);
    }

    /**
     *
     * @param url 图片地址
     * @param iv 填充的控件
     * @param placeholderRes 占位图
     */
    public static void loadImg(Context context,String url, ImageView iv,int placeholderRes){
        Glide.with(context)
                .load(url)
                .placeholder(placeholderRes)
                .into(iv);
    }
    /**
     *
     * @param url 图片地址
     * @param iv 填充的控件
     * @param placeholderRes 占位图
     */
    public static void loadImgNoAnimation(Context context,String url, ImageView iv,int placeholderRes){
        Glide.with(context)
                .load(url)
                .placeholder(placeholderRes)
                .dontAnimate()
                .skipMemoryCache(true)
                .into(iv);
    }
    /**
     *
     * @param url 图片地址
     * @param iv 填充的控件
     * @param placeholderRes 占位图
     * @param errorRes 加载错误
     */
    public static void loadImg(Context context,String url, ImageView iv,int placeholderRes,int errorRes){
        Glide.with(context)
                .load(url)
                .placeholder(placeholderRes)
                .error(errorRes)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
    }

    /**
     *
     * @param context 上下文
     * @param url   图片地址
     * @param iv   填充的控件
     * @param placeholderRes  占位图
     */
    public static void loadCircleImg(Context context,String url,ImageView iv,int placeholderRes){
        Glide.with(context)
                .load(url)
                .placeholder(placeholderRes)
//                .bitmapTransform(new GlideCircleTransform(context))
                .into(iv);
    }

    /**
     * 加载bitmap并转换成圆形图片
     * @param context 上下文
     * @param bmp bitmap
     * @param iv 填充的控件
     */
    public static void loadCircleBitmapImage(Context context, Bitmap  bmp, ImageView iv){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        Glide.with(context)
                .load(bytes)
                .bitmapTransform(new GlideCircleTransform(context))
                .into(iv);
    }


    /**
     * 加载圆形图片(本地)
     * @param context 上下文
     * @param resId   图片id
     * @param iv   填充的控件
     * @param placeholderRes  占位图
     */
    public static void loadCircleImg(Context context,int resId,ImageView iv,int placeholderRes){
        Glide.with(context)
                .load(resourceIdToUri(context,resId))
                .placeholder(placeholderRes)
                .bitmapTransform(new GlideCircleTransform(context))
                .into(iv);
    }
    /***
     * 加载本地图片 用glide转换成圆形
     * @param context 上下文
     * @param resId 资源id
     * @param iv 填充的imageView
     */
    public static void loadDiskCircleImg(Context context,int resId,ImageView iv){
        Glide.with(context)
                .load(resourceIdToUri(context,resId))
                .bitmapTransform(new GlideCircleTransform(context))
                .into(iv);
    }

    /**
     * 加载本地图片
     * @param context  上下文
     * @param resId 资源id
     * @param iv  填充的imageView
     */
    public static void loadDiskImg(Context context,int resId,ImageView iv){
        Glide.with(context)
                .load(resourceIdToUri(context,resId))
                .centerCrop()
                .into(iv);
    }

    private static Uri resourceIdToUri(Context context,int resId){
        return Uri.parse(ANDROID_RESOURCE+context.getPackageName()+FORWARD_SLASH+resId);
    }
    public static void loadDiskUriImg(Context context,Uri uri,ImageView iv){
        Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(iv);
    }

//    /**
//     * 修正图片地址
//     * @param uri
//     * @return
//     */
//    public static String fixImageUri(String uri){
//         if (uri.startsWith("http://")){
//            return uri;
//        }else{
//            return Constant.API1URL+uri;
//        }
//    }

//    /**
//     * 随机本地图片加载
//     * @return
//     */
//    public static int randomImg(){
//        int[] imgs = new int[]{R.drawable.ky_nan, R.drawable.ky_nv, R.drawable.yj_nan, R.drawable.yj_nv};
//        int img = imgs[(int) (1 + Math.random() * (3 - 1 + 1))];
//        return img;
//    }
}
