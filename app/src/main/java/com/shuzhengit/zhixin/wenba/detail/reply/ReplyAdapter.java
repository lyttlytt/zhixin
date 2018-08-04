package com.shuzhengit.zhixin.wenba.detail.reply;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.AskComment;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.util.TimeUtil;
import com.shuzhengit.zhixin.view.CircleImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/8 19:59
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int ITEM_HEADER=0;
    private static final int ITEM_COMMENT =1;
    private Context mContext;
    private AskWithReply mAskWithReply;
    private List<AskComment> mComments = new ArrayList<>();

    private OnItemAgreeAskListener mOnItemAgreeAskListener =null;
    private OnItemAgreeCommentListener mOnItemAgreeCommentListener = null;
    private OnItemClickUserListener mOnItemClickUserListener=null;


    public interface OnItemClickUserListener{
        void onItemClickUserListener(int memberId);
    }

    public void setOnItemClickUserListener(OnItemClickUserListener onItemClickUserListener) {
        mOnItemClickUserListener = onItemClickUserListener;
    }

    public void setOnItemAgreeCommentListener(OnItemAgreeCommentListener onItemAgreeCommentListener) {
        mOnItemAgreeCommentListener = onItemAgreeCommentListener;
    }

    public AskWithReply getAskWithReply() {
        return mAskWithReply;
    }
    public void onRefresh(List<AskComment> comments){
        this.mComments.clear();
        this.mComments.addAll(comments);
        notifyDataSetChanged();
    }
    public void addMore(List<AskComment> comments){
        this.mComments.addAll(comments);
        notifyDataSetChanged();
    }
    public void setOnItemAgreeAskListener(OnItemAgreeAskListener onItemAgreeAskListener) {
        mOnItemAgreeAskListener = onItemAgreeAskListener;
    }
    public interface OnItemAgreeCommentListener{
        void onItemAgreeAsk(AskComment askWithReply, TextView tvAddOne);
    }

    public interface OnItemAgreeAskListener{
        void onItemAgreeAsk(AskWithReply askWithReply, TextView tvAddOne);
    }
    public ReplyAdapter(Context context) {
        mContext = context;
    }

    public void setAskWithReply(AskWithReply askWithReply) {
        mAskWithReply = askWithReply;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return ITEM_HEADER;
        }else {
            return ITEM_COMMENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==ITEM_HEADER){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_reply_header,parent,false);
            return new ReplyHeaderViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_reply_comment,parent,false);
            return new ReplyCommentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType==ITEM_HEADER) {
            onBindHeaderViewHolder((ReplyHeaderViewHolder) holder, position);
        }else {
            onBindCommentViewHolder((ReplyCommentViewHolder) holder,position-1);
        }
    }

    private void onBindCommentViewHolder(ReplyCommentViewHolder holder, int position) {
        AskComment askComment = mComments.get(position);
        String replayUserAvatarUrl = askComment.getReplayUserAvatarUrl();
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickUserListener!=null){
                    mOnItemClickUserListener.onItemClickUserListener(askComment.getReplayUserId());
                }
            }
        });
        holder.mCivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickUserListener!=null){
                    mOnItemClickUserListener.onItemClickUserListener(askComment.getReplayUserId());
                }
            }
        });
        if (TextUtils.isEmpty(replayUserAvatarUrl)){
            holder.mCivAvatar.setImageResource(R.drawable.ic_normal_icon);
        }else {
            GlideLoadImageUtils.loadImg(mContext,replayUserAvatarUrl,holder.mCivAvatar);
        }
        holder.tvName.setText(askComment.getReplayUserNickname());
        holder.tvInfo.setText(askComment.getContent());
        if (askComment.getIsUp()==0){
            holder.tvAgreeCount.setText(String.valueOf(askComment.getAgreeCount()));
            holder.tvAgreeCount.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.grey500));
            Drawable resourceDrawable = ResourceUtils.getResourceDrawable(mContext, R.drawable.ic_unlike);
            resourceDrawable.setBounds(0,0,resourceDrawable.getIntrinsicWidth(),resourceDrawable.getIntrinsicHeight());
            holder.tvAgreeCount.setCompoundDrawables(resourceDrawable,null,null,null);
        }else {
            holder.tvAgreeCount.setText(String.valueOf(askComment.getAgreeCount()));
            holder.tvAgreeCount.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.colorPrimary));
            Drawable resourceDrawable = ResourceUtils.getResourceDrawable(mContext, R.drawable.ic_like);
            resourceDrawable.setBounds(0,0,resourceDrawable.getIntrinsicWidth(),resourceDrawable.getIntrinsicHeight());
            holder.tvAgreeCount.setCompoundDrawables(resourceDrawable,null,null,null);
        }
        holder.tvAgreeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemAgreeCommentListener!=null){
                    mOnItemAgreeCommentListener.onItemAgreeAsk(askComment,holder.tvAddOne);
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date parse = sdf.parse(askComment.getReplayTime());
            String timeFormatText = TimeUtil.getTimeFormatText(parse);
            holder.tvCommentTime.setText(timeFormatText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void onBindHeaderViewHolder(ReplyHeaderViewHolder holder, int position) {
        if (mAskWithReply!=null){
            holder.tvQuestionUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickUserListener!=null){
                        mOnItemClickUserListener.onItemClickUserListener(mAskWithReply.getUserId());
                    }
                }
            });
            holder.mCivQuestionAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickUserListener!=null){
                        mOnItemClickUserListener.onItemClickUserListener(mAskWithReply.getUserId());
                    }
                }
            });
            holder.tvAdminName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickUserListener!=null){
                        mOnItemClickUserListener.onItemClickUserListener(mAskWithReply.getReplayUserId());
                    }
                }
            });
            holder.civAdminAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickUserListener!=null){
                        mOnItemClickUserListener.onItemClickUserListener(mAskWithReply.getReplayUserId());
                    }
                }
            });
            if (TextUtils.isEmpty(mAskWithReply.getAvatarUrl())){
                holder.mCivQuestionAvatar.setImageResource(R.drawable.ic_normal_icon);
            }else {
                GlideLoadImageUtils.loadImg(mContext,mAskWithReply.getAvatarUrl(),holder.mCivQuestionAvatar);
            }
            if (TextUtils.isEmpty(mAskWithReply.getReplayavatarUrl())){
                holder.civAdminAvatar.setImageResource(R.drawable.ic_normal_icon);
            }else {
                GlideLoadImageUtils.loadImg(mContext,mAskWithReply.getReplayavatarUrl(),holder.civAdminAvatar);
            }
            holder.tvQuestionInfo.setText(mAskWithReply.getDescription());
            holder.tvQuestionUserName.setText(mAskWithReply.getNickName());
            holder.tvAdminName.setText(mAskWithReply.getReplayNickName());
            holder.tvReplyInfo.setText(mAskWithReply.getContent());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(mAskWithReply.getGmtCreate());
                String timeFormatText = TimeUtil.getTimeFormatText(date);
                holder.tvDate.setText(timeFormatText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tvCommentCount.setText(mAskWithReply.getAnswerCount()+"评论");
            if (mAskWithReply.getIsSupport()==0){
                holder.tvAgreeCount.setText(String.valueOf(mAskWithReply.getLikeCount()));
                holder.tvAgreeCount.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.grey500));
                Drawable resourceDrawable = ResourceUtils.getResourceDrawable(mContext, R.drawable.ic_unlike);
                resourceDrawable.setBounds(0,0,resourceDrawable.getIntrinsicWidth(),resourceDrawable.getIntrinsicHeight());
                holder.tvAgreeCount.setCompoundDrawables(resourceDrawable,null,null,null);
            }else {
                holder.tvAgreeCount.setText(String.valueOf(mAskWithReply.getLikeCount()));
                holder.tvAgreeCount.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.colorPrimary));
                Drawable resourceDrawable = ResourceUtils.getResourceDrawable(mContext, R.drawable.ic_like);
                resourceDrawable.setBounds(0,0,resourceDrawable.getIntrinsicWidth(),resourceDrawable.getIntrinsicHeight());
                holder.tvAgreeCount.setCompoundDrawables(resourceDrawable,null,null,null);
            }

            holder.tvAgreeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemAgreeAskListener!=null){
                        mOnItemAgreeAskListener.onItemAgreeAsk(mAskWithReply,holder.tvAddOne);
                    }
                }
            });
            holder.tvCommentCount.setText(mAskWithReply.getReadCount()+"评论");
        }
    }

    @Override
    public int getItemCount() {
        return 1+mComments.size();
    }
    public class ReplyHeaderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mCivQuestionAvatar,civAdminAvatar;
        TextView tvQuestionUserName,tvQuestionInfo,tvAdminName,tvReplyInfo,tvDate,tvCommentCount,tvAgreeCount,tvAddOne;
        public ReplyHeaderViewHolder(View itemView) {
            super(itemView);
            mCivQuestionAvatar = (CircleImageView) itemView.findViewById(R.id.civQuestionAvatar);
            tvQuestionUserName = (TextView) itemView.findViewById(R.id.tvQuestionUserName);
            tvQuestionInfo = (TextView) itemView.findViewById(R.id.tvQuestionInfo);
            civAdminAvatar = (CircleImageView) itemView.findViewById(R.id.civAdminAvatar);
            tvAdminName = (TextView) itemView.findViewById(R.id.tvAdminName);
            tvReplyInfo = (TextView) itemView.findViewById(R.id.tvReplyInfo);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
            tvAgreeCount = (TextView) itemView.findViewById(R.id.tvAgreeCount);
            tvAddOne = (TextView) itemView.findViewById(R.id.tvAddOne);
        }
    }
    public class ReplyCommentViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mCivAvatar;
        TextView tvName,tvAgreeCount,tvInfo,tvCommentTime,tvAddOne,tvCommentCount;
        public ReplyCommentViewHolder(View itemView) {
            super(itemView);
            mCivAvatar = (CircleImageView) itemView.findViewById(R.id.civAdmin);
            tvName = (TextView) itemView.findViewById(R.id.tvAdminName);
            tvAgreeCount =  (TextView) itemView.findViewById(R.id.tvAgreeCount);
            tvInfo =  (TextView) itemView.findViewById(R.id.tvAdminInfo);
            tvCommentTime =  (TextView) itemView.findViewById(R.id.tvCommentTime);
            tvAddOne = (TextView) itemView.findViewById(R.id.tvAddOne);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
        }
    }
}
