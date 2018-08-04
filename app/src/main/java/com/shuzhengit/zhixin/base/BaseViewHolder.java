package com.shuzhengit.zhixin.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;

/**
 * 和baseRecyclerViewAdapter搭配使用的holder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;
    private final View mConvertView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
//        itemView.setOnClickListener(v -> onItemClick(getAdapterPosition()));
    }

    public static BaseViewHolder getViewHolder(View view) {
        return new BaseViewHolder(view);
    }

    public <T extends View> T getViewById(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mConvertView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public void setText(int resId, String s) {
        TextView textView = getViewById(resId);
        textView.setText(s);
    }

    public void setTextAppend(int resId, String s) {
        TextView textView = getViewById(resId);
        textView.append(s);
    }

    public void setBackgroundDrawable(int resId, Drawable drawable) {
        View view = getViewById(resId);
        ViewCompat.setBackground(view, drawable);
//        getViewById(resId).setBackground(drawable);
    }

    public void setTextColor(int resId, int color) {

        ((TextView) getViewById(resId)).setTextColor(color);
    }

    public void setImageResource(Context context,int resId, int imageId, boolean isCircle) {
        if (!isCircle) {
            ((ImageView) getViewById(resId)).setImageResource(imageId);
        } else {
            GlideLoadImageUtils.loadDiskCircleImg(context, imageId, ((ImageView) getViewById(resId)));
        }
    }

    public void setImageResource(Context context,int resId, String url, boolean isCircle) {
        if (TextUtils.isEmpty(url)) {
            ((ImageView)getViewById(resId)).setImageResource(R.drawable.ic_normal_icon);
            return;
        }
        if (!isCircle) {
            GlideLoadImageUtils.loadImg(context, url, (ImageView) getViewById(resId));
        } else {
            GlideLoadImageUtils.loadImg(context, url, (ImageView) getViewById(resId));
        }
    }
}
