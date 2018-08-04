package com.shuzhengit.zhixin.index.document.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.DocumentPicture;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.TextUtil;
import com.shuzhengit.zhixin.util.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/19 11:02
 * E-mail:yuancongbin@gmail.com
 */

public class MultipleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MultipleAdapter";
    private static final int ITEMTYPE = 4;
    private static final int ITEM_TYPE_ONE = 0;
    private static final int ITEM_TYPE_TWO = 1;
    private static final int ITEM_TYPE_THREE = 2;
    private static final int ITEM_TYPE_FOUR = 3;
    private List<Document> mDocuments = new ArrayList<>();


    public MultipleAdapter() {
    }

    public interface OnTypeOneItemClickListener {
        void onTypeOneItemClickListener(Document document);

    }

    public interface OnTypeTwoItemClickListener {
        void onTypeTwoItemClickListener(Document document);

    }

    public interface OnTypeThreeItemClickListener {
        void onTypeThreeItemClickListener(Document document);

        void onTypeThreeRemoveItemListener(Document document);
    }
    public interface OnTypeFourItemClickListener{
        void onTypeFourItemClickListener(Document document);
    }

    private OnTypeOneItemClickListener mOnTypeOneItemClickListener = null;
    private OnTypeTwoItemClickListener mOnTypeTwoItemClickListener = null;
    private OnTypeThreeItemClickListener mOnTypeThreeItemClickListener = null;
    private OnTypeFourItemClickListener mOnTypeFourItemClickListener = null;

    public void setOnTypeOneItemClickListener(OnTypeOneItemClickListener onTypeOneItemClickListener) {
        mOnTypeOneItemClickListener = onTypeOneItemClickListener;
    }

    public void setOnTypeTwoItemClickListener(OnTypeTwoItemClickListener onTypeTwoItemClickListener) {
        mOnTypeTwoItemClickListener = onTypeTwoItemClickListener;
    }

    public void setOnTypeThreeItemClickListener(OnTypeThreeItemClickListener onTypeThreeItemClickListener) {
        mOnTypeThreeItemClickListener = onTypeThreeItemClickListener;
    }

    public void setOnTypeFourItemClickListener(OnTypeFourItemClickListener onTypeFourItemClickListener) {
        mOnTypeFourItemClickListener = onTypeFourItemClickListener;
    }

    public void addMore(List<Document> data) {
        mDocuments.addAll(data);
        notifyDataSetChanged();
    }

    public void onRefresh(List<Document> documents) {
        this.mDocuments.clear();
        this.mDocuments.addAll(documents);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mDocuments.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_TWO:
                return onCreateTypeTwoViewHolder(parent, viewType);
            case ITEM_TYPE_THREE:
                return onCreateTypeThreeViewHolder(parent, viewType);
            case ITEM_TYPE_FOUR:
                return onCreateTypeFourViewHolder(parent, viewType);
            case ITEM_TYPE_ONE:
            default:
                return onCreateTypeOneViewHolder(parent, viewType);
        }
    }

    private ItemDocumentFourHolder onCreateTypeFourViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_type_two, parent, false);
        return new ItemDocumentFourHolder(view);
    }

    /**
     * 类型三条目
     *
     * @param parent   recyclerView
     * @param viewType 2
     * @return ItemNewsThreeHolder
     */
    private ItemDocumentThreeHolder onCreateTypeThreeViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_type_three, parent, false);
        return new ItemDocumentThreeHolder(view);
    }

    /**
     * 类型二的条目
     *
     * @param parent   recyclerView
     * @param viewType 1
     * @return ItemNewsTwoHolder
     */
    private ItemDocumentTwoHolder onCreateTypeTwoViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_type_two, parent, false);
        return new ItemDocumentTwoHolder(view);
    }

    /**
     * 类型一的条目
     *
     * @param parent   recyclerView
     * @param viewType 0
     * @return ItemNewsOneHolder
     */
    private ItemDocumentOneHolder onCreateTypeOneViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_type_one, parent, false);
        return new ItemDocumentOneHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ITEM_TYPE_TWO:
                onBindTypeTwoViewHolder((ItemDocumentTwoHolder) holder, position);
                break;
            case ITEM_TYPE_THREE:
                onBindTypeThreeViewHolder((ItemDocumentThreeHolder) holder, position);
                break;
            case ITEM_TYPE_FOUR:
                onBindTypeFourViewHolder((ItemDocumentFourHolder) holder, position);
                break;
            case ITEM_TYPE_ONE:
            default:
                onBindTypeOneViewHolder((ItemDocumentOneHolder) holder, position);
                break;
        }
    }

    private void onBindTypeFourViewHolder(ItemDocumentFourHolder holder, int position) {
        Document document = mDocuments.get(position);
        int topFlag = document.getTopFlag();
        if (topFlag==1){
            holder.tvTopFlag.setVisibility(View.VISIBLE);
        }else {
            holder.tvTopFlag.setVisibility(View.GONE);
        }
        holder.tvReadCount.setText(String.valueOf(document.getReadCount()));
        holder.mTvAuthor.setText(document.getSourceName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        try {
            Date date = sdf.parse(document.getShowTime());
            String timeFormatText = TimeUtil.getTimeFormatText(date);
            String format = sdf.format(date);
//            String[] split = format.split("-");
//            String s = split[1] + split[2];
            String s = format.substring(5,format.length());
//            holder.tvDate.setText(s);
            holder.tvDate.setText(timeFormatText);
//            holder.tvDate.setText(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mLlImages.setVisibility(View.GONE);
        String title = document.getTitle();
        String s = TextUtil.ToDBC(title);
        holder.tvDocumentTitle.setText(s);
//        holder.tvDocumentTitle.setText(document.getTitle());
        for (int i = 0; i < holder.images.length; i++) {
            holder.images[i].setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mOnTypeFourItemClickListener != null) {
                mOnTypeFourItemClickListener.onTypeFourItemClickListener(document);
            }
        });
    }

    /**
     * 设置类型一的数据
     *
     * @param holder   ItemNewsOneHolder
     * @param position 全部数据中的下标
     */
    private void onBindTypeOneViewHolder(ItemDocumentOneHolder holder, int position) {

        Document document = mDocuments.get(position);
        int topFlag = document.getTopFlag();
        if (topFlag==1){
            holder.tvTopFlag.setVisibility(View.VISIBLE);
        }else {
            holder.tvTopFlag.setVisibility(View.GONE);
        }
        holder.tvReadCount.setText(String.valueOf(document.getReadCount()));
        holder.mTvAuthor.setText(document.getSourceName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        try {
            Date date = sdf.parse(document.getShowTime());
            String timeFormatText = TimeUtil.getTimeFormatText(date);
            String format = sdf.format(date);
//            String[] split = format.split("-");
            String s = format.substring(5,format.length());
//            String s = split[1] + split[2];
//            holder.tvDate.setText(s);
            holder.tvDate.setText(timeFormatText);
//            holder.tvDate.setText(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GlideLoadImageUtils.loadImg(holder.itemView.getContext(), document.getFirstPicPath().get(0).getSrc(), holder
                .ivBanner, R.drawable.loading_img);
        String s = TextUtil.ToDBC(document.getTitle());
        holder.tvDocumentTitle.setText(s);
//        holder.tvDocumentTitle.setText(document.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTypeOneItemClickListener != null) {
                    mOnTypeOneItemClickListener.onTypeOneItemClickListener(document);
                }
            }
        });
    }

    /**
     * 设置类型二的数据
     *
     * @param holder   ItemNewsTwoHolder
     * @param position 全部数据中的下标
     */
    private void onBindTypeTwoViewHolder(ItemDocumentTwoHolder holder, int position) {

        Document document = mDocuments.get(position);
        if (document.getTopFlag()==1){
            holder.tvTopFlag.setVisibility(View.VISIBLE);
        }else {
            holder.tvTopFlag.setVisibility(View.GONE);
        }
        holder.tvReadCount.setText(String.valueOf(document.getReadCount()));
        holder.mTvAuthor.setText(document.getSourceName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = sdf.parse(document.getShowTime());
            String timeFormatText = TimeUtil.getTimeFormatText(date);
            String format = sdf.format(date);
            String s = format.substring(5,format.length());
            holder.tvDate.setText(timeFormatText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<DocumentPicture> firstPicPath = document.getFirstPicPath();
        String s = TextUtil.ToDBC(document.getTitle());
        holder.tvDocumentTitle.setText(s);
        for (int i = 0; i < holder.images.length; i++) {
            GlideLoadImageUtils.loadImg(holder.itemView.getContext(), firstPicPath.get(i).getSrc(), holder.images[i], R
                    .drawable.loading_img);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mOnTypeTwoItemClickListener != null) {
                mOnTypeTwoItemClickListener.onTypeTwoItemClickListener(document);
            }
        });
    }

    /**
     * 设置类型三的数据
     *
     * @param holder   ItemNewsThreeHolder
     * @param position 全部数据中的下标
     */
    private void onBindTypeThreeViewHolder(ItemDocumentThreeHolder holder, int position) {

        Document document = mDocuments.get(position);
        if (document.getTopFlag()==1){
            holder.tvTopFlag.setVisibility(View.VISIBLE);
        }else {
            holder.tvTopFlag.setVisibility(View.GONE);
        }
        holder.tvAuthor.setText(document.getSourceName());
        List<DocumentPicture> firstPicPath = document.getFirstPicPath();
        String s = TextUtil.ToDBC(document.getTitle());
        holder.tvDocumentTitle.setText(s);
        GlideLoadImageUtils.loadImg(holder.itemView.getContext(), firstPicPath.get(0).getSrc(), holder.ivBanner, R.drawable
                .loading_img);
        holder.itemView.setOnClickListener(v -> {
            if (mOnTypeThreeItemClickListener != null) {
                mOnTypeThreeItemClickListener.onTypeThreeItemClickListener(document);
            }
        });
        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTypeThreeItemClickListener != null) {
                    mOnTypeThreeItemClickListener.onTypeThreeRemoveItemListener(document);
                    mDocuments.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDocuments.size();
    }
}
