package com.shuzhengit.zhixin.question.answer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.AnswerComment;
import com.shuzhengit.zhixin.bean.DocumentPicture;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.HtmlUtil;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/28 16:24
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AnswerCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_QUESTION_TITLE = 0;
    private static final int ITEM_ANSWER = 1;
    private static final int ITEM_ANSWER_COMMENT = 2;

    private QuestionAnswer mQuestionAnswer;
    private String mQuestionTitle = "";
    private List<AnswerComment> mAnswerComments = new ArrayList<>();
    private Context mContext;
    private List<DocumentPicture> imgs = new ArrayList<>();
    private boolean isLoadHtml = false;
    private OnContentItemImage mOnContentItemImage = null;
    private OnItemUserClickListener mOnItemUserClickListener = null;
    private OnItemAgreeCommentClickListener mOnItemAgreeCommentClickListener = null;

    public void setOnItemAgreeCommentClickListener(OnItemAgreeCommentClickListener onItemAgreeCommentClickListener) {
        mOnItemAgreeCommentClickListener = onItemAgreeCommentClickListener;
    }

    public interface OnItemAgreeCommentClickListener {
        void onItemAgreeComment(AnswerComment answerComment, TextView tvAgree, TextView tvAddOne);
    }

    public interface OnItemUserClickListener {
        void onItemUserClick(int memberId);
    }

    public void setOnContentItemImage(OnContentItemImage onContentItemImage) {
        mOnContentItemImage = onContentItemImage;
    }

    public void refreshAnswerComment(List<AnswerComment> comments) {
        this.mAnswerComments.clear();
        this.mAnswerComments.addAll(comments);
        notifyDataSetChanged();
    }

    public interface OnContentItemImage {
        void onContentItemImage(String url);
    }


    public AnswerCommentAdapter(Context context) {
        mContext = context;
    }

    public void setQuestionTitle(String questionTitle) {
        mQuestionTitle = questionTitle;
        notifyDataSetChanged();
    }

    public void setQuestionAnswer(QuestionAnswer questionAnswer) {
        mQuestionAnswer = questionAnswer;
        isLoadHtml = false;
        imgs.clear();
        imgs.addAll(questionAnswer.getAllPic());
        notifyDataSetChanged();
    }

    public void addMore(List<AnswerComment> data) {
        mAnswerComments.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnItemUserClickListener(OnItemUserClickListener onItemUserClickListener) {
        mOnItemUserClickListener = onItemUserClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_QUESTION_TITLE;
        } else if (position == 1) {
            return ITEM_ANSWER;
        } else {
            return ITEM_ANSWER_COMMENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_QUESTION_TITLE:
                return onCreateQuestionTitleViewHolder(parent, viewType);
            case ITEM_ANSWER:
                return onCreateAnswerHeaderViewHolder(parent, viewType);
            case ITEM_ANSWER_COMMENT:
            default:
                return onCreateAnswerCommentViewHolder(parent, viewType);
        }
    }

    private QuestionTitleViewHolder onCreateQuestionTitleViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_answer_question_title, parent, false);
        return new QuestionTitleViewHolder(view);
    }

    private AnswerCommentViewHolder onCreateAnswerCommentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_answer_comment, parent, false);
        return new AnswerCommentViewHolder(view);
    }

    private AnswerContentViewHolder onCreateAnswerHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_answer_content, parent, false);
        return new AnswerContentViewHolder(view);
    }

    private int getItemAnswerHeaderCount() {
        if (mQuestionAnswer == null) {
            return 0;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_QUESTION_TITLE:
                onBindQuestionTitleViewHolder((QuestionTitleViewHolder) holder, position);
                break;
            case ITEM_ANSWER:
                onBindAnswerHeaderViewHolder((AnswerContentViewHolder) holder, position);
                break;
            case ITEM_ANSWER_COMMENT:
            default:
                onBindAnswerCommentViewHolder((AnswerCommentViewHolder) holder, position - getItemAnswerHeaderCount());
                break;
        }
    }

    private void onBindQuestionTitleViewHolder(QuestionTitleViewHolder holder, int position) {
        holder.mTvTitle.setText(mQuestionTitle);
    }

    private void onBindAnswerHeaderViewHolder(AnswerContentViewHolder holder, int position) {
        if (!isLoadHtml) {
            holder.tvAnswerUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemUserClickListener != null) {
                        mOnItemUserClickListener.onItemUserClick(mQuestionAnswer.getReplayUserId());
                    }
                }
            });
            holder.civAnswerUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemUserClickListener != null) {
                        mOnItemUserClickListener.onItemUserClick(mQuestionAnswer.getReplayUserId());
                    }
                }
            });
            holder.tvAnswerUserName.setText(mQuestionAnswer.getReplayUserNickname());
            GlideLoadImageUtils.loadImg(mContext, mQuestionAnswer.getReplayUserAvatarUrl(), holder.civAnswerUserIcon);
            holder.tvAnswerTime.setText(mQuestionAnswer.getReplayTime());
            String css = "<style type=\"text/css\"> img {" +
                    "width:100%;" +
                    "height:auto;" +
                    "}" +
                    "body {" +
                    "margin-right:30px;" +
                    "margin-left:30px;" +
                    "margin-top:30px;" +
                    "font-size:80px;" +
                    "margin-right:30px" +
                    "}" +
                    "</style>";
            String html = "";
            if (mQuestionAnswer != null) {
                String s = changeDocumentDetail(mQuestionAnswer.getContent());
                LogUtils.i("TAG", s);
                html = "<html><body>" + s + "</body></html>";
            }
            holder.htmlTextView.setRichText(html);
            holder.htmlTextView.setTextSize(20);
            isLoadHtml = true;
            holder.htmlTextView.setOnImageClickListener(new HtmlTextView.OnImageClickListener() {
                @Override
                public void imageClicked(List<String> imageUrls, int position) {
                    if (mOnContentItemImage != null) {
                        mOnContentItemImage.onContentItemImage(imageUrls.get(position));
                    }
                }
            });
        }
    }

    private void onBindAnswerCommentViewHolder(AnswerCommentViewHolder holder, int position) {
        AnswerComment answerComment = mAnswerComments.get(position);
        holder.mTvUserName.setText(answerComment.getReplayUserNickname());
        GlideLoadImageUtils.loadImg(mContext, answerComment.getReplayUserAvatarUrl(), holder.mCivAvatar);
        holder.mTvAgree.setText(String.valueOf(answerComment.getAgreeCount()));
        holder.mTvAnswerComment.setText(answerComment.getContent());
        holder.mTvAnswerCommentTime.setText(answerComment.getReplayTime());
        if (answerComment.getIsUp() == 0) {
            holder.mTvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemAgreeCommentClickListener != null) {
                        mOnItemAgreeCommentClickListener.onItemAgreeComment(answerComment, holder.mTvAgree, holder
                                .mTvAddOne);
                    }
                }
            });
        } else {
            Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable.ic_like);
            holder.mTvAgree.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
            likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
            holder.mTvAgree.setCompoundDrawables(likeIcon, null, null, null);
        }

        holder.mTvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemUserClickListener != null) {
                    mOnItemUserClickListener.onItemUserClick(answerComment.getReplayUserId());
                }
            }
        });
        holder.mCivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemUserClickListener != null) {
                    mOnItemUserClickListener.onItemUserClick(answerComment.getReplayUserId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getItemAnswerHeaderCount() + mAnswerComments.size();
    }

    class QuestionTitleViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTitle;

        public QuestionTitleViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
        }
    }

    class AnswerContentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAnswerUserIcon;
        TextView tvAnswerUserName;
        HtmlTextView htmlTextView;
        TextView tvAnswerTime;

        public AnswerContentViewHolder(View itemView) {
            super(itemView);
            civAnswerUserIcon = (CircleImageView) itemView.findViewById(R.id.civAnswerUserIcon);
            tvAnswerUserName = (TextView) itemView.findViewById(R.id.tvAnswerUserName);
            htmlTextView = (HtmlTextView) itemView.findViewById(R.id.htmlTextView);
            tvAnswerTime = (TextView) itemView.findViewById(R.id.tvAnswerTime);
        }
    }

    class AnswerCommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCivAvatar;
        TextView mTvUserName, mTvAgree, mTvAddOne, mTvAnswerComment, mTvAnswerCommentTime;

        public AnswerCommentViewHolder(View itemView) {
            super(itemView);
            mCivAvatar = (CircleImageView) itemView.findViewById(R.id.ivAvatar);
            mTvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            mTvAgree = (TextView) itemView.findViewById(R.id.tvAgree);
            mTvAddOne = (TextView) itemView.findViewById(R.id.tvAddOne);
            mTvAnswerComment = (TextView) itemView.findViewById(R.id.tvAnswerComment);
            mTvAnswerCommentTime = (TextView) itemView.findViewById(R.id.tvAnswerCommentTime);
        }
    }


    /**
     * 对获得的文章详情判断是否有图片，
     * 如果有图片则通过替换字符串的方式获取图片url
     * <p>
     * //     * @param newsDetail
     */
    public String changeDocumentDetail(String content) {
        if (isChange(imgs)) {
            return changeDocumentBody(imgs, content);
        }
        return null;
    }

    /**
     * 判断是否有图片集合
     *
     * @param imgSrcs
     * @return
     */
    private boolean isChange(List<DocumentPicture> imgSrcs) {
        return imgSrcs != null && imgSrcs.size() >= 0;
    }

    /**
     * 将图片替换为能够直接使用的标签，即将<!--IMG#3-->  =》<img src="xxx">
     *
     * @param imgSrcs      获取到的图片资源数组
     * @param documentBody 修改后的文章详情
     * @return
     */
    private String changeDocumentBody(List<DocumentPicture> imgSrcs, String documentBody) {
        String oldChars = "";
        String newChars = "";
        for (int i = 0; i < imgSrcs.size(); i++) {
            oldChars = "<!--IMG#" + i + "-->";
            // 在客户端解决WebView图片屏幕适配的问题，在<img标签下添加style='max-width:90%;height:auto;'即可
            // 如："<img" + " style=max-width:100%;height:auto; " + "src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>"
//            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>";
//            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>";
//            newChars = imgSrcs.get(i).getSrc()+"?x-oss-process=style/default";
            List<String> imgSrc = HtmlUtil.getImgSrc(imgSrcs.get(i).getSrc());
            String s = imgSrc.get(0);
            newChars = "<img" + " src=\"" + s+"?x-oss-process=style/default" + "\"" + "/>";
//            newChars = imgSrcs.get(i).getSrc();
            documentBody = documentBody.replace(oldChars, newChars);
        }
        return documentBody;
    }
}
