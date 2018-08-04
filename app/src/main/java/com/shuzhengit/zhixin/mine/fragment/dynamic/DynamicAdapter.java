package com.shuzhengit.zhixin.mine.fragment.dynamic;

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
import com.shuzhengit.zhixin.bean.Dynamic;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 16:21
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Dynamic> mDynamics = new ArrayList<>();
    public interface OnItemClickListener{
        void onDocumentItemClick(int documentId,String esId);
        void onQuestionItemClick(int questionId);
        void onItemShareClick(Dynamic dynamic);
    }
    private OnItemClickListener mOnItemClickListener = null;

    private static final int TYPE_DOCUMENT = 0;
    private static final int TYPE_QUESTION = 1;
    private static final int TYPE_ANSWER = 2;
    private Context mContext;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public DynamicAdapter(Context context) {
        mContext = context;
    }
    public void addDynamics(List<Dynamic> dynamics){
        this.mDynamics.addAll(dynamics);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        Dynamic dynamic = mDynamics.get(position);
        switch (dynamic.getType()) {
            case TYPE_DOCUMENT:
                return TYPE_DOCUMENT;
            case TYPE_QUESTION:
                return TYPE_QUESTION;
            case TYPE_ANSWER:
                return TYPE_ANSWER;
            default:
                return TYPE_DOCUMENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DOCUMENT:
                return onCreateDocumentViewHolder(parent, viewType);
            case TYPE_QUESTION:
                return onCreateQuestionViewHolder(parent, viewType);
            case TYPE_ANSWER:
                return onCreateAnswerViewHolder(parent, viewType);
            default:
                return onCreateDocumentViewHolder(parent, viewType);
        }
    }

    private RecyclerView.ViewHolder onCreateAnswerViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic_qa, parent, false);
        return new QuestionViewHolder(view);
    }

    private RecyclerView.ViewHolder onCreateQuestionViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic_qa, parent, false);
        return new QuestionViewHolder(view);
    }

    private RecyclerView.ViewHolder onCreateDocumentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_QUESTION:
                onBindQuestionViewHolder((QuestionViewHolder) holder, position);
                break;
            case TYPE_ANSWER:
                onBindAnswerViewHolder((QuestionViewHolder) holder, position);
                break;
            case TYPE_DOCUMENT:
            default:
                onBindDocumentViewHolder((DocumentViewHolder) holder, position);
        }
    }

    private void onBindDocumentViewHolder(DocumentViewHolder holder, int position) {
        Dynamic dynamic = mDynamics.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onDocumentItemClick(dynamic.getContentId(),dynamic.getElcIndexId());
                }
            }
        });

        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemShareClick(dynamic);
                }
            }
        });
        if (TextUtils.isEmpty(dynamic.getAvatarUrl())) {
            holder.mUserAvatar.setImageResource(R.drawable.ic_normal_icon);
        } else {
            GlideLoadImageUtils.loadImg(mContext, dynamic.getAvatarUrl(), holder.mUserAvatar);
        }
        holder.tvReleaseTime.setText(dynamic.getGmtCreate());
        holder.tvReplyContent.setText(dynamic.getContent());
        holder.tvArticleContent.setText(dynamic.getTitle());
        holder.tvUserName.setText(dynamic.getUserName());
        if (TextUtils.isEmpty(dynamic.getPic())){
            holder.ivArticlePic.setImageResource(R.drawable.ic_dynamic_placeholder);
        }else {
            GlideLoadImageUtils.loadImg(mContext, dynamic.getPic(), holder.ivArticlePic);
        }
        holder.tvAgreeCount.setText(dynamic.getLikeCount()+" 赞");
        holder.tvCommentCount.setText(String.valueOf(dynamic.getCommentCount()));
    }

    private void onBindQuestionViewHolder(QuestionViewHolder holder, int position) {
        Dynamic dynamic = mDynamics.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onQuestionItemClick(dynamic.getContentId());
                }
            }
        });
        if (TextUtils.isEmpty(dynamic.getAvatarUrl())) {
            holder.mUserAvatar.setImageResource(R.drawable.ic_normal_icon);
        } else {
            GlideLoadImageUtils.loadImg(mContext, dynamic.getAvatarUrl(), holder.mUserAvatar);
        }
        holder.tvReleaseTime.setText(dynamic.getGmtCreate());
        holder.tvReplyContent.setText(dynamic.getContent());
        holder.tvArticleContent.setText(dynamic.getTitle());
        holder.tvUserName.setText(dynamic.getUserName());
        if (!TextUtils.isEmpty(dynamic.getPic())){
            GlideLoadImageUtils.loadImg(mContext, dynamic.getPic(), holder.ivArticlePic);
        }else {
            holder.ivArticlePic.setImageResource(R.drawable.ic_dynamic_placeholder);
        }
//        holder.tvAgreeCount.setText(dynamic.getLikeCount()+" 赞");
    }

    private void onBindAnswerViewHolder(QuestionViewHolder holder, int position) {
        Dynamic dynamic = mDynamics.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onQuestionItemClick(dynamic.getContentId());
                }
            }
        });
        if (TextUtils.isEmpty(dynamic.getAvatarUrl())) {
            holder.mUserAvatar.setImageResource(R.drawable.ic_normal_icon);
        } else {
            GlideLoadImageUtils.loadImg(mContext, dynamic.getAvatarUrl(), holder.mUserAvatar);
        }
        holder.tvReleaseTime.setText(dynamic.getGmtCreate());
        holder.tvReplyContent.setText(dynamic.getContent());
        holder.tvArticleContent.setText(dynamic.getTitle());
        holder.tvUserName.setText(dynamic.getUserName());
        if (!TextUtils.isEmpty(dynamic.getPic())){
            GlideLoadImageUtils.loadImg(mContext, dynamic.getPic(), holder.ivArticlePic);
        }else {
            holder.ivArticlePic.setImageResource(R.drawable.ic_dynamic_placeholder);
        }
    }


    @Override
    public int getItemCount() {
        return mDynamics.size();
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mUserAvatar;
        ImageView ivArticlePic;
        TextView tvUserName;
        TextView tvReleaseTime;
        TextView tvReplyContent;
        TextView tvArticleContent;
        LinearLayout llAgree;
        LinearLayout llComment;
        LinearLayout llShare;
        TextView tvAgreeCount;
        TextView tvCommentCount;
        public DocumentViewHolder(View itemView) {
            super(itemView);
            mUserAvatar = (CircleImageView) itemView.findViewById(R.id.civUserAvatar);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvReleaseTime = (TextView) itemView.findViewById(R.id.tvReleaseTime);
            tvReplyContent = (TextView) itemView.findViewById(R.id.tvReplyContent);
            ivArticlePic = (ImageView) itemView.findViewById(R.id.ivArticlePic);
            tvArticleContent = (TextView) itemView.findViewById(R.id.tvArticleContent);
            llAgree = (LinearLayout) itemView.findViewById(R.id.llAgree);
            llComment = (LinearLayout) itemView.findViewById(R.id.llComment);
            llShare = (LinearLayout) itemView.findViewById(R.id.llShare);
            tvAgreeCount = (TextView) itemView.findViewById(R.id.tvAgreeCount);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
        }
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mUserAvatar;
        ImageView ivArticlePic;
        TextView tvUserName;
        TextView tvReleaseTime;
        TextView tvReplyContent;
        TextView tvArticleContent;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            mUserAvatar = (CircleImageView) itemView.findViewById(R.id.civUserAvatar);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvReleaseTime = (TextView) itemView.findViewById(R.id.tvReleaseTime);
            tvReplyContent = (TextView) itemView.findViewById(R.id.tvReplyContent);
            ivArticlePic = (ImageView) itemView.findViewById(R.id.ivArticlePic);
            tvArticleContent = (TextView) itemView.findViewById(R.id.tvArticleContent);
        }
    }
}
