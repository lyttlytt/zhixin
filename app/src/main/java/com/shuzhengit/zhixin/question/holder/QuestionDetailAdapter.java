package com.shuzhengit.zhixin.question.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.DocumentPicture;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
import com.shuzhengit.zhixin.index.document.PhotoViewActivity;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.HtmlUtil;
import com.shuzhengit.zhixin.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/4 09:40
 * E-mail:yuancongbin@gmail.com
 */

public class QuestionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "QuestionDetailAdapter";
    private static final int ITEM_QUESTION_HEADER = 0;
    private static final int ITEM_QUESTION_COMMENT = 1;
    private static final int ITEM_QUESTION_NO_ANSWER = 2;
    private Question mQuestion;
    private List<QuestionAnswer> mAnswers = new ArrayList<>();
    private Context mContext;
    private OnAnswerItemListener mOnAnswerItemListener = null;
    private OnItemUserClickListener mOnItemUserClickListener = null;
    private OnQuestionFollowerClickListener mOnQuestionFollowerClickListener = null;


    public QuestionDetailAdapter(Context context) {
        mContext = context;
    }

    public void setQuestion(Question question) {
        mQuestion = question;
        notifyDataSetChanged();
    }

    public void setOnQuestionFollowerClickListener(OnQuestionFollowerClickListener onQuestionFollowerClickListener) {
        mOnQuestionFollowerClickListener = onQuestionFollowerClickListener;
    }

    public interface OnQuestionFollowerClickListener {
        void onFollowQuestion(Question question1, Question question, TextView tvFollowed);
    }

    public void setOnItemUserClickListener(OnItemUserClickListener onItemUserClickListener) {
        mOnItemUserClickListener = onItemUserClickListener;
    }

    public void addMoreQuestionAnswer(List<QuestionAnswer> mAnswers) {
        this.mAnswers.addAll(mAnswers);
        notifyDataSetChanged();
    }

    public void refreshQuestionAnswer(List<QuestionAnswer> answers) {
        this.mAnswers.clear();
        this.mAnswers.addAll(answers);
        notifyDataSetChanged();
    }

    public interface OnAnswerItemListener {
        void onItemClick(QuestionAnswer questionAnswer, String questionTitle, int questionId);

        void onItemAgree(QuestionAnswer questionAnswer, TextView tvAgree, TextView tvAddOne);
    }

    public interface OnItemUserClickListener {
        void onItemUserClick(int userId);
    }

    public void setOnAnswerItemListener(OnAnswerItemListener onAnswerItemListener) {
        mOnAnswerItemListener = onAnswerItemListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_QUESTION_NO_ANSWER:
                return onCreateNoAnswerViewHolder(parent, viewType);
            case ITEM_QUESTION_HEADER:
                return onCreateQuestionDetailTitleViewHolder(parent, viewType);
            case ITEM_QUESTION_COMMENT:
                return onCreateQuestionDetailCommentViewHolder(parent, viewType);
            default:
                return onCreateQuestionDetailCommentViewHolder(parent, viewType);
        }
    }

    private RecyclerView.ViewHolder onCreateNoAnswerViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_comment, parent, false);
        return new ItemQuestionNoAnswerHolder(view);
    }

    public ItemQuestionDetailTitleHolder onCreateQuestionDetailTitleViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_detail_title, parent,
                false);
        return new ItemQuestionDetailTitleHolder(view);
    }

    public ItemQuestionDetailCommentHolder onCreateQuestionDetailCommentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_detail_comment, parent,
                false);
        return new ItemQuestionDetailCommentHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemQuestionHeader()) {
            return ITEM_QUESTION_HEADER;
        } else if (mAnswers.size() == 0) {
            return ITEM_QUESTION_NO_ANSWER;
        } else {
            return ITEM_QUESTION_COMMENT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ITEM_QUESTION_NO_ANSWER:
                onBindQuestionNoAnswerViewHolder((ItemQuestionNoAnswerHolder) holder, position);
                break;
            case ITEM_QUESTION_HEADER:
                onBindQuestionDetailTitleViewHolder((ItemQuestionDetailTitleHolder) holder, position);
                break;
            case ITEM_QUESTION_COMMENT:
            default:
                onBindQuestionDetailCommentViewHolder((ItemQuestionDetailCommentHolder) holder, position -
                        getItemQuestionHeader());
                break;
        }
    }

    private void onBindQuestionNoAnswerViewHolder(ItemQuestionNoAnswerHolder holder, int position) {
        holder.tvNoContent.setText("暂时没有回答");
    }

    private void onBindQuestionDetailCommentViewHolder(ItemQuestionDetailCommentHolder holder, int position) {
        QuestionAnswer questionAnswer = mAnswers.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAnswerItemListener != null && mQuestion != null) {
                    mOnAnswerItemListener.onItemClick(questionAnswer, mQuestion.getTitle(),mQuestion.getId());
                }
            }
        });
        if (questionAnswer.getVoteType()==0){
            holder.mTvAgreeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAnswerItemListener!=null){
                        mOnAnswerItemListener.onItemAgree(questionAnswer,holder.mTvAgreeCount,holder.mTvAddOne);
                    }
                }
            });
        }else {
            Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable.ic_like);
            holder.mTvAgreeCount.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
            likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
            holder.mTvAgreeCount.setCompoundDrawables(likeIcon, null, null, null);
        }
        String content = questionAnswer.getContent();
        List<DocumentPicture> allPic = questionAnswer.getAllPic();
        LogUtils.d(TAG,allPic.toString());
        if (allPic!=null && allPic.size()!=0) {
            content = changeDocumentBody(allPic, content);
            holder.mIvImg.setVisibility(View.VISIBLE);
            LogUtils.d(TAG,allPic.get(0).getSrc());
            List<String> imgSrc = HtmlUtil.getImgSrc(allPic.get(0).getSrc());
            GlideLoadImageUtils.loadImg(mContext,imgSrc.get(0)+"?x-oss-process=style/default",holder.mIvImg);
        }else {
            holder.mIvImg.setVisibility(View.GONE);
        }
        holder.mTvAnswer.setText(content);
        holder.mTvAgreeCount.setText(String.valueOf(questionAnswer.getAgreeCount()));
        GlideLoadImageUtils.loadImg(mContext, questionAnswer.getReplayUserAvatarUrl(), holder.mReplyUserAvatar);
        holder.mTvReplyUserName.setText(questionAnswer.getReplayUserNickname());
        int voteType = questionAnswer.getVoteType();
        if (voteType == 0) {
            holder.mTvAgreeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAnswerItemListener != null) {
                        mOnAnswerItemListener.onItemAgree(questionAnswer, holder.mTvAgreeCount, holder.mTvAddOne);
                    }
                }
            });
        } else {
            Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable
                    .ic_like);
            holder.mTvAgreeCount.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
            likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
            holder.mTvAgreeCount.setCompoundDrawables(likeIcon, null, null, null);
            holder.mTvAgreeCount.setClickable(false);
        }
        holder.mReplyUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemUserClickListener != null) {
                    mOnItemUserClickListener.onItemUserClick(questionAnswer.getReplayUserId());
                }
            }
        });
        holder.mTvReplyUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemUserClickListener != null) {
                    mOnItemUserClickListener.onItemUserClick(questionAnswer.getReplayUserId());
                }
            }
        });

    }

    private void onBindQuestionDetailTitleViewHolder(ItemQuestionDetailTitleHolder holder, int position) {
        if (mQuestion != null) {
            holder.tvQuestion.setText(mQuestion.getTitle());
            String description="";
            if (TextUtils.isEmpty(mQuestion.getDescription())){
                description = "暂无描述";
            }else {
                description = mQuestion.getDescription();
            }
            SpannableString spannableString = new SpannableString("描述:" + description);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ResourceUtils.getResourceColor(APP
                    .getInstance(), R.color.grey500));
            spannableString.setSpan(foregroundColorSpan, 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(12, true), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvAnswerCount.setText(String.format(ResourceUtils.getResourceString(APP.getInstance(), R.string
                    .questionAnswerCount), mQuestion.getAnswerCount()));
            holder.tvQuestionDescription.setText(spannableString);
            holder.tvReleaseUserName.setText(mQuestion.getNickName());
            if (TextUtils.isEmpty(mQuestion.getAvatarUrl())){
                holder.mUserAvatar.setImageResource(R.drawable.ic_normal_icon);
            }else {
                GlideLoadImageUtils.loadImg(mContext,mQuestion.getAvatarUrl(),holder.mUserAvatar);
            }
            holder.llReleaseUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId",mQuestion.getUserId());
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            int imageType = mQuestion.getImageType();
            if (imageType == 1) {

                for (int i = 0; i < holder.images.length; i++) {
//                    if (mQuestion.getImages().size() == 1) {
//                        if (i == 0) {
//                            GlideLoadImageUtils.loadImg(mContext, mQuestion.getImages().get(0), holder.images[i], R
//                                    .drawable
//                                    .shape_img_placeholder);
//                            holder.images[i].setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(mContext, PhotoViewActivity.class);
//                                    intent.putExtra(EventCodeUtils.DOCUMENT_PICTURE, mQuestion.getImages().get(0));
//                                    mContext.startActivity(intent);
//                                }
//                            });
//                        } else {
//                            holder.images[i].setVisibility(View.INVISIBLE);
//                            holder.images[i].setClickable(false);
//                        }
//                    }else {
                        if (i<mQuestion.getImages().size()){
                            GlideLoadImageUtils.loadImg(mContext, mQuestion.getImages().get(i), holder.images[i], R
                                    .drawable
                                    .loading_img);
                            int finalI = i;
                            holder.images[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, PhotoViewActivity.class);
                                    intent.putExtra(EventCodeUtils.DOCUMENT_PICTURE, mQuestion.getImages().get(finalI));
                                    mContext.startActivity(intent);
                                }
                            });
                        }else {
                            holder.images[i].setVisibility(View.INVISIBLE);
                            holder.images[i].setClickable(false);
                        }
//                    }
                }
            } else if (imageType == 2) {
                for (int i = 0; i < holder.images.length; i++) {
                    GlideLoadImageUtils.loadImg(mContext, mQuestion.getImages().get(i), holder.images[i], R.drawable
                            .loading_img);
                    int finalI = i;
                    holder.images[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PhotoViewActivity.class);
                            intent.putExtra(EventCodeUtils.DOCUMENT_PICTURE, mQuestion.getImages().get(finalI));
                            mContext.startActivity(intent);
                        }
                    });
                }
            } else {
                holder.llImage.setVisibility(View.GONE);
            }
            if (mQuestion.getIsLike()==1){
                holder.tvFollowed.setText("已关注");
                holder.tvFollowed.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color
                        .grey500));
            }else {
                holder.tvFollowed.setText("+ 添加关注");
                holder.tvFollowed.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color
                        .colorPrimary));
            }
            holder.tvFollowed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnQuestionFollowerClickListener != null) {
                        mOnQuestionFollowerClickListener.onFollowQuestion(mQuestion,mQuestion,holder.tvFollowed);
                    }
                }
            });
        }

    }

    public int getItemQuestionHeader() {
        if (mQuestion == null) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getItemQuestionComment() {
        return mAnswers.size();
    }

    @Override
    public int getItemCount() {
        if (getItemQuestionComment() == 0) {
            return getItemQuestionHeader() + 1;
        } else {
            return getItemQuestionHeader() + getItemQuestionComment();
        }
    }


    private String changeDocumentBody(List<DocumentPicture> imgSrcs, String documentBody) {
        String oldChars = "";
        String newChars = "";
        String[] split = documentBody.split("<!--IMG#0-->");
        return split[0];
//        for (int i = 0; i < imgSrcs.size(); i++) {
//            oldChars = "<!--IMG#" + i + "-->";
//            // 在客户端解决WebView图片屏幕适配的问题，在<img标签下添加style='max-width:90%;height:auto;'即可
//            // 如："<img" + " style=max-width:100%;height:auto; " + "src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>"
////            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>";
////            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>";
//            newChars = "[图片]";
//            documentBody = documentBody.replace(oldChars, newChars);
//        }
//        LogUtils.e("TAG",documentBody);
//        return documentBody;
    }


}
