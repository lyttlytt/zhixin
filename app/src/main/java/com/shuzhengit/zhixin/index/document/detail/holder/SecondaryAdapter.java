package com.shuzhengit.zhixin.index.document.detail.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/18 13:11
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class SecondaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_COMMENT = 0;
    private final int ITEM_ALL_SECONDARY_COMMENT_TAB = 1;
    private final int ITEM_SECONDARY_COMMENT = 2;
    private final int ITEM_NO_COMMENT = 3;
    private Comment mComment = null;
    private List<Comment> mSecondaryComments = new ArrayList<>();
    private Context mContext;
    private OnItemClickUserListener mOnItemClickUserListener=null;

    public void setOnItemClickUserListener(OnItemClickUserListener onItemClickUserListener) {
        mOnItemClickUserListener = onItemClickUserListener;
    }

    public interface OnItemClickUserListener {
        void onItemClickUserListener(int memberId);
    }

    public interface OnItemAgreeListener {
        void onSecondaryCommentAgree(Comment comment, TextView tvAgree, TextView tvAddOne);

        void onCommentAgree(Comment comment, TextView tvAgree, TextView tvAddOne);
    }

    private OnItemAgreeListener mOnItemAgreeListener = null;

    public void setOnItemAgreeListener(OnItemAgreeListener onItemAgreeListener) {
        mOnItemAgreeListener = onItemAgreeListener;
    }

    public SecondaryAdapter(Context context) {
        mContext = context;
    }

    public int getCommentCount() {
//        if (mComment == null)
//            return 0;
//        else
        return 1;
    }

    public int getAllSecondaryCommentTabCount() {
        if (mComment == null) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getSecondaryCommentCount() {
        return mSecondaryComments.size();
//        return 20;
    }

    public void addComment(Comment comment) {
        mSecondaryComments.add(0, comment);
        notifyDataSetChanged();
    }

    public void setComment(Comment comment) {
        mComment = comment;
        notifyDataSetChanged();
    }

    public void setSecondaryComments(List<Comment> secondaryComments) {
        mSecondaryComments = secondaryComments;
        notifyDataSetChanged();
//        notifyItemRangeChanged(2, getSecondaryCommentCount());
    }

    public void addMoreSecondaryComments(List<Comment> secondaryComments) {
        mSecondaryComments.addAll(secondaryComments);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int secondaryCommentCount = getSecondaryCommentCount();

        if (position == 0) {
            return ITEM_COMMENT;
        } else if (position == 1) {
            return ITEM_ALL_SECONDARY_COMMENT_TAB;
        } else if (secondaryCommentCount == 0) {
            return ITEM_NO_COMMENT;
        } else {
            return ITEM_SECONDARY_COMMENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_COMMENT:
                return onCreateCommentViewHolder(parent, viewType);
            case ITEM_ALL_SECONDARY_COMMENT_TAB:
                return onCreateAllSecondaryCommentTabViewHolder(parent, viewType);
            case ITEM_SECONDARY_COMMENT:
                return onCreateSecondaryCommentViewHolder(parent, viewType);
            case ITEM_NO_COMMENT:
                return onCreateSecondaryNoComment(parent, viewType);
            default:
                return onCreateSecondaryCommentViewHolder(parent, viewType);
        }
    }

    private RecyclerView.ViewHolder onCreateSecondaryNoComment(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_no_comment, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    private RecyclerView.ViewHolder onCreateAllSecondaryCommentTabViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tab_secondary_comment, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    private ItemDocumentCommentHolder onCreateSecondaryCommentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document_comment, parent, false);
        return new ItemDocumentCommentHolder(view);
    }

    private ItemDocumentCommentHolder onCreateCommentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document_comment, parent, false);
        return new ItemDocumentCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ITEM_COMMENT:
                onBindCommentViewHolder((ItemDocumentCommentHolder) holder, position);
                break;
            case ITEM_ALL_SECONDARY_COMMENT_TAB:
                break;
            case ITEM_NO_COMMENT:
                break;
            case ITEM_SECONDARY_COMMENT:
                onBindSecondaryCommentViewHolder((ItemDocumentCommentHolder) holder, position - 2);
                break;
            default:break;
        }
    }

    private void onBindSecondaryCommentViewHolder(ItemDocumentCommentHolder holder, int position) {
        holder.setIsRecyclable(false);
        Comment comment = mSecondaryComments.get(position);
        holder.mTvAgree.setText(String.valueOf(comment.getLikeCount()));
        holder.mTvComment.setText(comment.getCommentContent());
        holder.mTvUserName.setText(comment.getCommentName());
        holder.mTvCommentTime.setText(comment.getGmtCreate());
        holder.mTvCommentCount.setVisibility(View.GONE);
        holder.mTvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickUserListener!=null){
                    mOnItemClickUserListener.onItemClickUserListener(comment.getCommentId());
                }
            }
        });
        holder.mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickUserListener!=null){
                    mOnItemClickUserListener.onItemClickUserListener(comment.getCommentId());
                }
            }
        });
        GlideLoadImageUtils.loadCircleImg(mContext, comment.getAvatar(), holder.mIvAvatar, R.drawable.ic_normal_icon);
        if (comment.getIsLike() == 0) {
            Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable
                    .ic_unlike);
            holder.mTvAgree.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.grey500));
            likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
            holder.mTvAgree.setCompoundDrawables(likeIcon, null, null, null);
            holder.mTvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemAgreeListener != null) {
                        mOnItemAgreeListener.onSecondaryCommentAgree(comment, holder.mTvAgree, holder.mTvAddOne);
                    }
                }
            });
        } else {
            Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable
                    .ic_like);
            holder.mTvAgree.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
            likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
            holder.mTvAgree.setCompoundDrawables(likeIcon, null, null, null);

        }
    }

    private void onBindCommentViewHolder(ItemDocumentCommentHolder holder, int position) {
        // TODO: 2017/8/18 等真实数据过来一些界面还需要隐藏,微调再设置
        holder.setIsRecyclable(false);
        if (mComment != null) {
            holder.mTvAgree.setText(String.valueOf(mComment.getLikeCount()));
            holder.mTvUserName.setText(mComment.getCommentName());
            holder.mTvCommentTime.setText(mComment.getGmtCreate());
            holder.mTvCommentCount.setVisibility(View.GONE);
            holder.mTvComment.setText(mComment.getCommentContent());
            GlideLoadImageUtils.loadCircleImg(mContext, mComment.getAvatar(), holder.mIvAvatar, R.drawable
                    .ic_normal_icon);
            if (mComment.getIsLike() != 0) {
                Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable.ic_like);
                holder.mTvAgree.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
                likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
                holder.mTvAgree.setCompoundDrawables(likeIcon, null, null, null);
            } else {
                Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable.ic_unlike);
                holder.mTvAgree.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.grey500));
                likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
                holder.mTvAgree.setCompoundDrawables(likeIcon, null, null, null);
                holder.mTvAgree.setOnClickListener(v -> {
                    if (mOnItemAgreeListener != null) {
                        mOnItemAgreeListener.onCommentAgree(mComment, holder.mTvAgree, holder.mTvAddOne);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (getSecondaryCommentCount() != 0) {
            return getCommentCount() + getAllSecondaryCommentTabCount() + getSecondaryCommentCount();
        } else {
            return getCommentCount() + getAllSecondaryCommentTabCount() + 1;
        }
    }
}
