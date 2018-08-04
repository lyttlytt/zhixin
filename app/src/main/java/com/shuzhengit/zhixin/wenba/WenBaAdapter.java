package com.shuzhengit.zhixin.wenba;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.FollowTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/8 09:09
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WenBaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_MINE_WEN_BA = 0;
    private static final int ITEM_WEN_BA = 1;
    private List<WenBa> mData = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;
    private OnItemFollowListener mOnItemFollowListener = null;
    private OnClickMineWenBaListener mOnClickMineWenBaListener = null;
    private OnClickMoreWenBaListener mOnClickMoreWenBaListener = null;

    public void setOnClickMineWenBaListener(OnClickMineWenBaListener onClickMineWenBaListener) {
        mOnClickMineWenBaListener = onClickMineWenBaListener;
    }

    public void setOnClickMoreWenBaListener(OnClickMoreWenBaListener onClickMoreWenBaListener) {
        mOnClickMoreWenBaListener = onClickMoreWenBaListener;
    }

    public interface OnClickMineWenBaListener{
        void onClick();
    }
    public interface OnClickMoreWenBaListener{
        void onClick();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemFollowListener(OnItemFollowListener onItemFollowListener) {
        mOnItemFollowListener = onItemFollowListener;
    }

    public interface OnItemFollowListener {
        void onItemFollow(WenBa wenBa);
    }

    public interface OnItemClickListener {
        void onItemClick(WenBa wenBa);
    }

    public WenBaAdapter(Context context) {
        mContext = context;
    }

    public void refresh(List<WenBa> data) {
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addMore(List<WenBa> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_MINE_WEN_BA;
        } else {
            return ITEM_WEN_BA;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_MINE_WEN_BA) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_wenba_title, parent, false);
            return new WenBaTitleViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_wenba, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_WEN_BA) {
            onBindWenBaListViewHolder((ViewHolder) holder, position - 1);
        } else {
            onBindWenBaTitleViewHolder((WenBaTitleViewHolder) holder, position);
        }
    }

    private void onBindWenBaTitleViewHolder(WenBaTitleViewHolder holder, int position) {
        holder.llMineWenBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickMineWenBaListener!=null){
                    mOnClickMineWenBaListener.onClick();
                }
            }
        });
        holder.llMoreWenBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickMoreWenBaListener!=null){
                    mOnClickMoreWenBaListener.onClick();
                }
            }
        });
    }

    private void onBindWenBaListViewHolder(ViewHolder holder, int position) {
        WenBa wenBa = mData.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(wenBa);
                }
            }
        });
        GlideLoadImageUtils.loadImg(mContext, wenBa.getBarImage(), holder.ivThumbnail);
        if (TextUtils.isEmpty(wenBa.getAvatarUrl())){
            holder.civAvatar.setImageResource(R.drawable.ic_normal_icon);
        }else {
            GlideLoadImageUtils.loadImg(mContext, wenBa.getAvatarUrl(), holder.civAvatar);
        }
        holder.tvAdminName.setText(wenBa.getNickName());
        holder.tvDescription.setText(wenBa.getWelcomeTitle());
        holder.tvProfession.setText(wenBa.getProfession());
        holder.tvAskCount.setText(String.format(ResourceUtils.getResourceString(mContext, R.string.askCount),
                wenBa.getAskCount()));
        holder.tvFollowCount.setText(String.format(ResourceUtils.getResourceString(mContext, R.string.userFollow),
                wenBa.getLikeCount()));
        holder.tvType.setText(String.format(ResourceUtils.getResourceString(mContext, R.string.wenBaType), wenBa
                .getColumnTitle()));
        if (wenBa.getIsUp() == 1) {
            holder.tvFollow.setFollow(true);
        } else {
            holder.tvFollow.setFollow(false);
        }
        holder.tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemFollowListener != null) {
                    mOnItemFollowListener.onItemFollow(wenBa);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        CircleImageView civAvatar;
        TextView tvAdminName, tvProfession, tvDescription, tvType, tvFollowCount, tvAskCount;
        FollowTextView tvFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
            civAvatar = (CircleImageView) itemView.findViewById(R.id.civAvatar);
            tvAdminName = (TextView) itemView.findViewById(R.id.tvAdminName);
            tvProfession = (TextView) itemView.findViewById(R.id.tvProfession);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvFollowCount = (TextView) itemView.findViewById(R.id.tvFollowCount);
            tvAskCount = (TextView) itemView.findViewById(R.id.tvAskCount);
            tvFollow = (FollowTextView) itemView.findViewById(R.id.tvFollow);
        }

    }

    public class WenBaTitleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMineWenBa, llMoreWenBa;

        public WenBaTitleViewHolder(View itemView) {
            super(itemView);
            llMineWenBa = (LinearLayout) itemView.findViewById(R.id.llMineWenBa);
            llMoreWenBa = (LinearLayout) itemView.findViewById(R.id.llMoreWenBa);
        }
    }
}
