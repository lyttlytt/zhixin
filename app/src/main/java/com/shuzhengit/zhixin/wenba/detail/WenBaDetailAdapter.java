package com.shuzhengit.zhixin.wenba.detail;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.util.DeviceUtil;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.util.TimeUtil;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.FollowTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/8 14:57
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WenBaDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int ITEM_INFO=0;
    private static final int ITEM_QUESTION=1;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;
    private WenBa mWenBa;
    private List<AskWithReply> data = new ArrayList<>();
    private OnItemFollowListener mOnItemFollowListener = null;
    private OnItemAgreeAskListener mOnItemAgreeAskListener =null;
    private boolean isNew = true;
    private boolean expanded = false;
    private OnAskIsNewOrHotListener mOnAskIsNewOrHotListener=null;

//    public interface OnModifyIconClickListener{
//        void onModifyIconClickListener();
//    }
    public boolean isNew() {
        return isNew;
    }

    public void setOnAskIsNewOrHotListener(OnAskIsNewOrHotListener onAskIsNewOrHotListener) {
        mOnAskIsNewOrHotListener = onAskIsNewOrHotListener;
    }

    public interface OnAskIsNewOrHotListener{
        void onNewOrHot(boolean isNew);
    }
    public interface OnItemClickUserListener{
        void onItemClickUserListener(int memberId);
    }
    private OnItemClickUserListener mOnItemClickUserListener=null;

    public void setOnItemClickUserListener(OnItemClickUserListener onItemClickUserListener) {
        mOnItemClickUserListener = onItemClickUserListener;
    }

    public void setOnItemAgreeAskListener(OnItemAgreeAskListener onItemAgreeAskListener) {
        mOnItemAgreeAskListener = onItemAgreeAskListener;
    }

    public interface OnItemAgreeAskListener{
        void onItemAgreeAsk(AskWithReply askWithReply, TextView tvAddOne);
    }
    public void setOnItemFollowListener(OnItemFollowListener onItemFollowListener) {
        mOnItemFollowListener = onItemFollowListener;
    }

    public interface OnItemFollowListener {
        void onItemFollow(WenBa wenBa);
    }

    public void setWenBa(WenBa wenBa) {
        mWenBa = wenBa;
        notifyDataSetChanged();
    }

    public void setData(List<AskWithReply> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void refreshData(List<AskWithReply> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(AskWithReply askWithReply);
    }
    public WenBaDetailAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return ITEM_INFO;
        }else {
            return ITEM_QUESTION;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==ITEM_INFO){
            View view = LayoutInflater.from(mContext).inflate( R.layout.item_wenba_detail_info,parent,false);
            return new WenBaDetailTitleViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_wenba_question,parent,false);
            return new WenBaQuestionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType==ITEM_QUESTION){
            onBindQuestionViewHolder((WenBaQuestionViewHolder)holder,position-1);
        }else {
            onBindWenBaDetailViewHolder((WenBaDetailTitleViewHolder) holder,position);
        }
    }

    private void onBindWenBaDetailViewHolder(WenBaDetailTitleViewHolder holder, int position) {
        if (mWenBa!=null){
//            if (isAdmin){
//                holder.ivModifyWenBa.setVisibility(View.VISIBLE);
//                holder.ivModifyWenBa.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mOnModifyIconClickListener!=null){
//                            mOnModifyIconClickListener.onModifyIconClickListener();
//                        }
//                    }
//                });
//            }else {
//                holder.ivModifyWenBa.setVisibility(View.GONE);
//            }
            holder.tvAdminName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickUserListener!=null){
                        mOnItemClickUserListener.onItemClickUserListener(mWenBa.getUserId());
                    }
                }
            });
            holder.civAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickUserListener!=null){
                        mOnItemClickUserListener.onItemClickUserListener(mWenBa.getUserId());
                    }
                }
            });
            holder.tvAdminName.setText(mWenBa.getNickName());
            holder.tvAdminInfo.setText(mWenBa.getDescription());
            holder.tvFollowCount.setText(String.valueOf(mWenBa.getLikeCount())+"关注");
            holder.tvAskCount.setText(String.format(ResourceUtils.getResourceString(mContext,R.string.askCount),
                    mWenBa.getAskCount()));
            holder.tvReplyCount.setText(String.format(ResourceUtils.getResourceString(mContext,R.string.comment),
                    mWenBa.getAnswerCount()));
            holder.tvProfession.setText(mWenBa.getProfession());
            holder.tvInfo.setText(mWenBa.getWelcomeTitle());

            if (TextUtils.isEmpty(mWenBa.getAvatarUrl())){
                holder.civAdmin.setImageResource(R.drawable.ic_normal_icon);
            }else {
                GlideLoadImageUtils.loadImg(mContext, mWenBa.getAvatarUrl(), holder.civAdmin);
            }
            GlideLoadImageUtils.loadImg(mContext,mWenBa.getBarImage(),holder.ivThumbnail);
            if (mWenBa.getIsUp()==0){
                holder.tvFollow.setFollow(false);
            }else {
                holder.tvFollow.setFollow(true);
            }
            holder.tvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemFollowListener!=null){
                        mOnItemFollowListener.onItemFollow(mWenBa);
                    }
                }
            });
            holder.tvHot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvHot.setBackground(ResourceUtils.getResourceDrawable(mContext,R.drawable.shape_wenba_solid));
                    holder.tvHot.setTextColor(Color.WHITE);
                    holder.tvNew.setBackground(null);
                    holder.tvNew.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.grey500));
                    isNew = false;
                    if (mOnAskIsNewOrHotListener!=null){
                        mOnAskIsNewOrHotListener.onNewOrHot(isNew);
                    }
//                    sortData();
//                    notifyDataSetChanged();
                }
            });
            holder.tvNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvNew.setBackground(ResourceUtils.getResourceDrawable(mContext,R.drawable.shape_wenba_solid));
                    holder.tvNew.setTextColor(Color.WHITE);
                    holder.tvHot.setBackground(null);
                    holder.tvHot.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.grey500));
                    isNew = true;
                    if (mOnAskIsNewOrHotListener!=null){
                        mOnAskIsNewOrHotListener.onNewOrHot(isNew);
                    }
                }
            });

            if (expanded){
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.tvAdminInfo.getLayoutParams();
                layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                holder.tvAdminInfo.setLayoutParams(layoutParams);
                holder.ivArrow.setImageResource(R.drawable.ic_wenba_description_up);
            }else {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.tvAdminInfo.getLayoutParams();
                layoutParams.height = DeviceUtil.dp2px(APP.getInstance(),70);
                holder.tvAdminInfo.setLayoutParams(layoutParams);
                holder.ivArrow.setImageResource(R.drawable.ic_wenba_description_down);
            }
            if (mWenBa.getDescription().length()<72){
                holder.ivArrow.setVisibility(View.GONE);
            }


            holder.ivArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!expanded){
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.tvAdminInfo.getLayoutParams();
                        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        holder.tvAdminInfo.setLayoutParams(layoutParams);
                        holder.ivArrow.setImageResource(R.drawable.ic_wenba_description_up);
                        expanded = true;
//                        holder.tvAdminInfo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
//                                .MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                        holder.ivArrow.setImageResource(R.drawable.ic_wenba_description_up);
                    }else {
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.tvAdminInfo.getLayoutParams();
                        layoutParams.height = DeviceUtil.dp2px(APP.getInstance(),70);
                        holder.tvAdminInfo.setLayoutParams(layoutParams);
                        holder.ivArrow.setImageResource(R.drawable.ic_wenba_description_down);
                        expanded = false;

//                        holder.tvAdminInfo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
//                                .MATCH_PARENT, DeviceUtil.dp2px(APP.getInstance(),70)));
//                        holder.ivArrow.setImageResource(R.drawable.ic_wenba_description_down);
                    }
                }
            });
        }
    }

    private void onBindQuestionViewHolder(WenBaQuestionViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        AskWithReply askWithReply = data.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(askWithReply);
                }
            }
        });
        holder.tvAskUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickUserListener!=null){
                    mOnItemClickUserListener.onItemClickUserListener(askWithReply.getUserId());
                }
            }
        });
        holder.civAskAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickUserListener!=null){
                    mOnItemClickUserListener.onItemClickUserListener(askWithReply.getUserId());
                }
            }
        });
        holder.tvAskUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickUserListener!=null){
                    mOnItemClickUserListener.onItemClickUserListener(askWithReply.getReplayUserId());
                }
            }
        });
        holder.civAdminAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickUserListener!=null){
                    mOnItemClickUserListener.onItemClickUserListener(askWithReply.getReplayUserId());
                }
            }
        });
        holder.tvAskUserName.setText(askWithReply.getNickName());
        holder.tvAskInfo.setText(askWithReply.getDescription());
        if (TextUtils.isEmpty(askWithReply.getAvatarUrl())){
            holder.civAskAvatar.setImageResource(R.drawable.ic_normal_icon);
        }else {
            GlideLoadImageUtils.loadImg(mContext, askWithReply.getAvatarUrl(), holder.civAskAvatar);
        }
        holder.tvAdminName.setText(askWithReply.getReplayNickName());
        if (TextUtils.isEmpty(askWithReply.getReplayavatarUrl())){
            holder.civAdminAvatar.setImageResource(R.drawable.ic_normal_icon);
        }else {
            GlideLoadImageUtils.loadImg(mContext, askWithReply.getReplayavatarUrl(), holder.civAdminAvatar);
        }
        holder.tvReplyInfo.setText(askWithReply.getContent());
        if (askWithReply.getIsSupport()==0){
            holder.tvAgreeCount.setText(String.valueOf(askWithReply.getLikeCount()));
            holder.tvAgreeCount.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.grey500));
            Drawable resourceDrawable = ResourceUtils.getResourceDrawable(mContext, R.drawable.ic_unlike);
            resourceDrawable.setBounds(0,0,resourceDrawable.getIntrinsicWidth(),resourceDrawable.getIntrinsicHeight());
            holder.tvAgreeCount.setCompoundDrawables(resourceDrawable,null,null,null);
        }else {
            holder.tvAgreeCount.setText(String.valueOf(askWithReply.getLikeCount()));
            holder.tvAgreeCount.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.colorPrimary));
            Drawable resourceDrawable = ResourceUtils.getResourceDrawable(mContext, R.drawable.ic_like);
            resourceDrawable.setBounds(0,0,resourceDrawable.getIntrinsicWidth(),resourceDrawable.getIntrinsicHeight());
            holder.tvAgreeCount.setCompoundDrawables(resourceDrawable,null,null,null);
        }
        holder.tvAgreeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemAgreeAskListener!=null){
                    mOnItemAgreeAskListener.onItemAgreeAsk(askWithReply,holder.tvAddOne);
                }
            }
        });
        holder.tvCommentCount.setText(askWithReply.getReadCount()+"评论");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date parse = sdf.parse(askWithReply.getGmtCreate());
            String timeFormatText = TimeUtil.getTimeFormatText(parse);
            holder.tvDate.setText(timeFormatText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 1+data.size();
    }
    public class WenBaDetailTitleViewHolder extends RecyclerView.ViewHolder{
        ImageView ivThumbnail,ivArrow;
        TextView tvInfo,tvFollowCount,tvAdminName,tvProfession,tvAdminInfo,tvAskCount,tvReplyCount,
                tvHot,tvNew;
        FollowTextView tvFollow;
        CircleImageView civAdmin;
        public WenBaDetailTitleViewHolder(View itemView) {
            super(itemView);
            ivThumbnail= (ImageView) itemView.findViewById(R.id.ivThumbnail);
            tvInfo = (TextView) itemView.findViewById(R.id.tvInfo);
            tvFollow = (FollowTextView) itemView.findViewById(R.id.tvFollow);
            tvFollowCount = (TextView) itemView.findViewById(R.id.tvFollowCount);
            civAdmin = (CircleImageView) itemView.findViewById(R.id.civAdmin);
            tvAdminName = (TextView) itemView.findViewById(R.id.tvAdminName);
            tvProfession = (TextView) itemView.findViewById(R.id.tvProfession);
            tvAdminInfo = (TextView) itemView.findViewById(R.id.tvAdminInfo);
            tvAskCount = (TextView) itemView.findViewById(R.id.tvAskCount);
            tvReplyCount = (TextView) itemView.findViewById(R.id.tvReplyCount);
            tvHot = (TextView) itemView.findViewById(R.id.tvHot);
            tvNew = (TextView) itemView.findViewById(R.id.tvNew);
            ivArrow = (ImageView) itemView.findViewById(R.id.ivArrow);
        }
    }
    public class WenBaQuestionViewHolder extends RecyclerView.ViewHolder{
        CircleImageView civAskAvatar,civAdminAvatar;
        TextView tvAskUserName,tvAskInfo,tvAdminName,tvReplyInfo,tvDate,tvAgreeCount,tvCommentCount,tvAddOne;
        public WenBaQuestionViewHolder(View itemView) {
            super(itemView);
            civAskAvatar = (CircleImageView) itemView.findViewById(R.id.civAskAvatar);
            tvAskUserName = (TextView) itemView.findViewById(R.id.tvAskUserName);
            tvAskInfo = (TextView) itemView.findViewById(R.id.tvAskInfo);
            civAdminAvatar = (CircleImageView) itemView.findViewById(R.id.civAdminAvatar);
            tvAdminName = (TextView) itemView.findViewById(R.id.tvAdminName);
            tvReplyInfo = (TextView) itemView.findViewById(R.id.tvReplyInfo);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvAgreeCount = (TextView) itemView.findViewById(R.id.tvAgreeCount);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
            tvAddOne = (TextView) itemView.findViewById(R.id.tvAddOne);
        }
    }
}
