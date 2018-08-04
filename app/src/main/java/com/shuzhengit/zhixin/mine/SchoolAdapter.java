package com.shuzhengit.zhixin.mine;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuzhengit.zhixin.bean.School;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/5 14:15
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder> {
    private List<School> mLocationSchools = new ArrayList<>();
    private List<School> mSearchSchool = new ArrayList<>();
    private boolean showLocationSchool = true;
    private OnItemSchoolClickListener mOnItemSchoolClickListener=null;

    public void setOnItemSchoolClickListener(OnItemSchoolClickListener onItemSchoolClickListener) {
        mOnItemSchoolClickListener = onItemSchoolClickListener;
    }

    public interface OnItemSchoolClickListener {
        void onItemSchoolClick(School school);
    }
    public SchoolAdapter() {
    }

    public void setLocationSchools(List<School> locationSchools) {
        mLocationSchools = locationSchools;
        notifyDataSetChanged();
    }

    public void searchInfo(List<School> schools) {
//        mSchools.clear();
        showLocationSchool = false;
        mSearchSchool = schools;
//        mSchools.addAll(schools);
        notifyDataSetChanged();
    }


    public void showLocationSchool() {
        showLocationSchool = true;
        notifyDataSetChanged();
    }

    @Override
    public SchoolAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 88);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(SchoolAdapter.ViewHolder holder, int position) {
        if (showLocationSchool) {
            holder.mTv.setText(mLocationSchools.get(position).getName());
            holder.mTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemSchoolClickListener!=null){
                        mOnItemSchoolClickListener.onItemSchoolClick(mLocationSchools.get(position));
                    }
                }
            });
        }
        else {
            holder.mTv.setText(mSearchSchool.get(position).getName());
            holder.mTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemSchoolClickListener!=null){
                        mOnItemSchoolClickListener.onItemSchoolClick(mSearchSchool.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (showLocationSchool) {
            return mLocationSchools.size();
        } else {
            return mSearchSchool.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView;
        }
    }
}
