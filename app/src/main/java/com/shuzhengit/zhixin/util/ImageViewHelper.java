package com.shuzhengit.zhixin.util;

import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/13 20:01
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:专门获取ImageView的宽高
 */

public class ImageViewHelper {
    //默认图片的宽高
    private static int DEFAULT_WIDTH = 200;
    private static int DEFAULT_HEIGHT = 200;

    /**
     * 获取ImageView的宽度
     * 1.getWidth(绘制完成,如果视图没有绘制完成没有值)
     * 2.layout_width(有可能设置的是wrap_content)
     * 3.max_width
     *
     * @param imageView 需要获取宽度的ImageView
     * @return
     */
    public static int getImageViewWidth(ImageView imageView) {
        if (imageView != null) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            int width = 0;
            if (layoutParams != null && layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = imageView.getWidth();
            }

            if (width <= 0 && layoutParams != null) {
                width = layoutParams.width;
            }
            if (width <= 0) {
                width = getImageViewFieldValue(imageView, "mMaxWidth");
            }
            return width;
        }
        return DEFAULT_WIDTH;
    }

    /**
     * 获取ImageView的高
     *
     * @param imageView 需要获取高度的ImageView
     * @return
     */
    public static int getImageViewHeight(ImageView imageView) {
        if (imageView != null) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            int height = 0;
            if (layoutParams != null && layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = imageView.getHeight();
            }
            if (height <= 0 && layoutParams != null) {
                height = layoutParams.height;
            }
            if (height<=0){
                height = getImageViewFieldValue(imageView,"mMaxHeight");
            }
            return height;
        }
        return DEFAULT_HEIGHT;
    }

    /**
     * 通过反射获取maxWidth/maxHeight设置的值
     *
     * @param imageView 需要获取宽度的ImageView
     * @param fieldName 需要反射的成员变量名称
     * @return
     */
    private static int getImageViewFieldValue(ImageView imageView, String fieldName) {
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (int) field.get(imageView);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                return fieldValue;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
