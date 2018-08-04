package com.shuzhengit.zhixin.base;

import android.view.View;


public interface OnItemClickListener<T> {
    abstract void onItemClick(View v, int position, T t);
}
