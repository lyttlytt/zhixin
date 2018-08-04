package com.shuzhengit.zhixin.index.document.detail.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.DocumentPicture;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.HtmlTextView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/27 15:30
 * E-mail:yuancongbin@gmail.com
 */

public class DocumentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;
    private static final int TYPE_RECOMMEND = 2;
    private static final int TYPE_COMMENT_TAB = 3;
    private static final int TYPE_COMMENT = 4;
    private static final int TYPE_NO_COMMENT = 5;
    private Context mContext;
    private List<DocumentPicture> imgs = new ArrayList<>();
    private WebSettings mWebSettings;
    private List<Comment> mComments = new ArrayList<>();
    private String s = "";
    String myBody = "";
    private boolean isLoadHtml = false;
    //    private DocumentDetailListener mDocumentDetailListener = null;
    private List<Document> mRecommendDocument = new ArrayList<>();
    //    private final AnimationSet mAnimationSet;
    private Document mDocument;


    public void setContent(Document document) {
        mDocument = document;
        isLoadHtml = false;
        imgs.clear();
        imgs.addAll(document.getAllPic());
        notifyDataSetChanged();
    }


    private OnDocumentAgree mOnDocumentAgree = null;
    private OnItemCommentAgree mOnItemCommentAgree = null;
    private OnCommentItemClickListener mOnCommentItemClickListener = null;
    private OnRecommendItemClickListener mOnRecommendItemClickListener = null;
    private OnContentItemImage mOnContentItemImage = null;
    private onItemClickUserListener mOnItemClickUserListener = null;

    public void setOnItemClickUserListener(onItemClickUserListener onItemClickUserListener) {
        mOnItemClickUserListener = onItemClickUserListener;
    }

    public void setOnDocumentAgree(OnDocumentAgree onDocumentAgree) {
        mOnDocumentAgree = onDocumentAgree;
    }

    public void setOnItemCommentAgree(OnItemCommentAgree onItemCommentAgree) {
        mOnItemCommentAgree = onItemCommentAgree;
    }

    public void setOnCommentItemClickListener(OnCommentItemClickListener onCommentItemClickListener) {
        mOnCommentItemClickListener = onCommentItemClickListener;
    }

    public void setOnRecommendItemClickListener(OnRecommendItemClickListener onRecommendItemClickListener) {
        mOnRecommendItemClickListener = onRecommendItemClickListener;
    }

    public void setOnContentItemImage(OnContentItemImage onContentItemImage) {
        mOnContentItemImage = onContentItemImage;
    }

    public interface OnDocumentAgree {
        void onDocumentAgree(Document document, TextView tvAddOne, TextView tvLikeCount, ImageView ivLikeIcon,
                             RelativeLayout llLike);
    }

    public interface OnItemCommentAgree {
        void onItemCommentAgree(Comment comment, TextView tvAddOne, TextView tvAgree);
    }

    public interface OnCommentItemClickListener {
        void onCommentItemClickListener(Comment comment);
    }

    public interface OnRecommendItemClickListener {
        void onRecommendItemClickListener(int position);
    }

    public interface OnContentItemImage {
        void onContentItemImage(DocumentPicture url);
    }

    public interface onItemClickUserListener {
        void onItemClickUserListener(int memberId);
    }

    public void addMore(List<Comment> comments) {
        this.mComments.addAll(comments);
        notifyDataSetChanged();
//        notifyItemRangeChanged(getItemContentCount()+getItemRecommendCount()+getItemTitleCount(),getItemCount());

    }

    public void setRecommendDocument(List<Document> recommendDocument) {
        mRecommendDocument = recommendDocument;
    }

    public DocumentDetailAdapter(Context context) {
        mContext = context;
//        imgs.add("http://pic-bucket.nosdn.127.net/photo/0001/2017-07-27/CQCR5HMM00AP0001NOS.jpg");
//        imgs.add("http://cms-bucket.nosdn.127.net/catchpic/9/90/907deb40d10d542fa98bc9cc04852148.jpg");
//        imgs.add("http://cms-bucket.nosdn.127.net/catchpic/5/57/57f27921e46e0d367ff99a060dfc7125.jpg");
//        imgs.add("http://cms-bucket.nosdn.127.net/catchpic/8/87/87b61919bf9b810b4333261191af1927.jpg");
//        imgs.add("http://cms-bucket.nosdn.127.net/catchpic/3/32/32d2d46cae414e5a4eb6b15520c7515a.jpg");
//        mAnimationSet = new AnimationSet(context, null);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
//        alphaAnimation.setDuration(1000);
//        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -50);
//        translateAnimation.setDuration(1000);
//        mAnimationSet.addAnimation(translateAnimation);
//        mAnimationSet.addAnimation(alphaAnimation);
    }


    public void setComment(List<Comment> comment) {
        this.mComments = comment;
        notifyDataSetChanged();
    }

    public void addComment(Comment s) {
        this.mComments.add(0, s);
        notifyDataSetChanged();
//        notifyItemInserted(getItemTitleCount() + getItemContentCount() + getItemRecommendCount());
    }

    public int getCommentTabCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        int itemTitleCount = getItemTitleCount();
        int itemContentCount = getItemContentCount();
        int itemRecommendCount = getItemRecommendCount();
        int itemCommentCount = getItemCommentCount();
        int commentTabCount = getCommentTabCount();
        if (position < itemTitleCount) {
            return TYPE_TITLE;
        } else if (position >= itemTitleCount && position < (itemTitleCount + itemContentCount)) {
            return TYPE_CONTENT;
        } else if (position >= (itemTitleCount + itemContentCount) && position < (itemTitleCount + itemContentCount +
                itemRecommendCount)) {
            return TYPE_RECOMMEND;
        } else if (position >= (itemTitleCount + itemContentCount + itemRecommendCount) && position < (itemTitleCount
                + itemContentCount +
                itemRecommendCount + commentTabCount)) {
            return TYPE_COMMENT_TAB;
        } else if (position >= (itemTitleCount + itemContentCount +
                itemRecommendCount + commentTabCount) && position < (itemTitleCount + itemContentCount +
                itemRecommendCount + itemCommentCount + commentTabCount)) {
            return TYPE_COMMENT;
        } else if (position >= (itemTitleCount + itemContentCount +
                itemRecommendCount + itemCommentCount + commentTabCount)) {
            return TYPE_NO_COMMENT;
        } else {
            return -1;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                return onCreateDocumentTitleViewHolder(parent, viewType);
            case TYPE_CONTENT:
                return onCreateDocumentContentViewHolder(parent, viewType);
            case TYPE_RECOMMEND:
                return onCreateDocumentRecommendViewHolder(parent, viewType);
            case TYPE_COMMENT_TAB:
                return onCreateCommentTabViewHolder(parent, viewType);
            case TYPE_COMMENT:
                return onCreateDocumentCommentViewHolder(parent, viewType);
            case TYPE_NO_COMMENT:
                return onCreateNoCommentViewHolder(parent, viewType);
            default:
                return null;
        }
    }

    private RecyclerView.ViewHolder onCreateCommentTabViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_all_comment_tab, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    private ItemNoCommentViewHolder onCreateNoCommentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_no_comment, parent, false);
        return new ItemNoCommentViewHolder(view);
    }

    private ItemDocumentCommentHolder onCreateDocumentCommentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document_comment, parent, false);
        return new ItemDocumentCommentHolder(view);
    }

    private ItemDocumentRecommendHolder onCreateDocumentRecommendViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document_recomment, parent, false);
        return new ItemDocumentRecommendHolder(view);
    }

    private ItemDocumentContentHolder onCreateDocumentContentViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document_content, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document_content,parent,false);
        return new ItemDocumentContentHolder(view);
    }

    private ItemDocumentTitleHolder onCreateDocumentTitleViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document_title, parent, false);
        return new ItemDocumentTitleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_TITLE:
                onBindTitleViewHolder((ItemDocumentTitleHolder) holder, position);
                break;
            case TYPE_CONTENT:
                onBindContentViewHolder((ItemDocumentContentHolder) holder, position - getItemTitleCount());
                break;
            case TYPE_RECOMMEND:
                onBindRecommendViewHolder((ItemDocumentRecommendHolder) holder, position - getItemTitleCount() -
                        getItemContentCount());
                break;
            case TYPE_COMMENT:
                onBindCommentViewHolder((ItemDocumentCommentHolder) holder, position - getItemTitleCount()
                        - getItemRecommendCount() - getItemContentCount() - getCommentTabCount());
                break;
            case TYPE_NO_COMMENT:
                onBindNoCommentViewHolder((ItemNoCommentViewHolder) holder, position);
            default:
                break;
        }
    }


    private void onBindNoCommentViewHolder(ItemNoCommentViewHolder holder, int position) {
    }

    private void onBindCommentViewHolder(ItemDocumentCommentHolder holder, int position) {
        holder.setIsRecyclable(false);
        Comment comment = this.mComments.get(position);
        holder.mTvComment.setText(comment.getCommentContent());
        if (comment.getCommentCount() == 0) {
            holder.mTvCommentCount.setText("回复");
            holder.mTvCommentCount.setPadding(0, 0, 0, 0);
            holder.mTvCommentCount.setBackground(null);
        } else {
            String format = String.format(ResourceUtils.getResourceString(APP.getInstance(), R.string.comment),
                    comment.getCommentCount());
            holder.mTvCommentCount.setText(format);
            holder.mTvCommentCount.setPadding(30, 10, 30, 10);
            holder.mTvCommentCount.setBackground(ResourceUtils.getResourceDrawable(holder.itemView.getContext(), R
                    .drawable.shape_comment_bg));
        }
        GlideLoadImageUtils.loadImg(mContext, comment.getAvatar(), holder.mIvAvatar);
        holder.mIvAvatar.setOnClickListener(v -> {
            if (mOnItemClickUserListener != null) {
                mOnItemClickUserListener.onItemClickUserListener(comment.getCommentId());
            }
        });
        holder.mTvUserName.setText(comment.getCommentName());
        holder.mTvUserName.setOnClickListener(v -> {
            if (mOnItemClickUserListener != null) {
                mOnItemClickUserListener.onItemClickUserListener(comment.getCommentId());
            }
        });
        holder.mTvAgree.setText(String.valueOf(comment.getLikeCount()));
        holder.mTvCommentTime.setText(comment.getGmtCreate());
        holder.itemView.setOnClickListener(v -> {
            if (mOnCommentItemClickListener != null) {
                mOnCommentItemClickListener.onCommentItemClickListener(comment);
            }
        });
        if (comment.getIsLike() != 0) {
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
                if (mOnItemCommentAgree != null) {
                    mOnItemCommentAgree.onItemCommentAgree(comment, holder.mTvAddOne, holder.mTvAgree);
                }
            });
        }
    }

    private void onBindRecommendViewHolder(ItemDocumentRecommendHolder holder, int position) {
        holder.mRlRecommendDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecommendItemClickListener != null) {
                    mOnRecommendItemClickListener.onRecommendItemClickListener(position);
                }
            }
        });
    }

    private void onBindContentViewHolder(ItemDocumentContentHolder holder, int position) {
//        holder.setIsRecyclable(false);
        if (!isLoadHtml) {
            String html = "";
            if (mDocument != null) {
                if (mDocument.getIsGrap() != 2) {
                    holder.itemView.requestFocus();
                    holder.mHtmlTextView.setVisibility(View.VISIBLE);
                    holder.mWebView.setVisibility(View.GONE);
//                    holder.itemView.requestFocus();
                    String s = changeDocumentDetail(mDocument.getContent());
                    holder.mHtmlTextView.setRichText("<html><body>" + s + "</body></html>");
                    holder.mHtmlTextView.setOnImageClickListener(new HtmlTextView.OnImageClickListener() {
                        @Override
                        public void imageClicked(List<String> imageUrls, int position) {
                            if (mOnContentItemImage != null) {
                                mOnContentItemImage.onContentItemImage(imgs.get(position));
                            }
                        }
                    });
                } else {
                    holder.itemView.requestFocus();
                    String content = mDocument.getContent();
                    String replace = content.replace("\n", "");
                    String replace1 = replace.replace("\\\"", "\"");
                    holder.mHtmlTextView.setVisibility(View.GONE);
                    holder.mWebView.setVisibility(View.VISIBLE);
//                    holder.mHtmlTextView.setRichText("<html><body>" + mDocument.getContent() + "</body></html>");

//                }else {
//                    mHtmlCode.append("<body>" + mDocument.getContent() + "</body></html>");
//                    holder.mWebView.setVisibility(View.VISIBLE);
                    holder.mHtmlTextView.setVisibility(View.GONE);
                    holder.mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams
 .MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    WebSettings settings = holder.mWebView.getSettings();
                    settings.setJavaScriptEnabled(true);
                    settings.setUseWideViewPort(true);

                    //设置自适应屏幕，两者合用~
                    settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
                    settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
                    settings.setDomStorageEnabled(true);
                    // 添加js交互接口类，并起别名 imagelistner
                    holder.mWebView.addJavascriptInterface(new JavascriptInterfaceImpl(mContext,replace1),
 "imageClickListener");
                    holder.mWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            view.getSettings().setJavaScriptEnabled(true);
                            super.onPageFinished(view, url);
                            // html加载完成之后，添加监听图片的点击js函数
//                    holder.mWebView.loadUrl();javascript
//                            holder.mWebView.loadUrl("javascript:" + "(function(){var objs = document .getElementsByTagName" +
//                                    "(\"img\"); for(var i=0;i<objs.length ;i++) {objs[i].onclick=function() {window" +
//                                    ".imageClickListener.openImage(this.src);}}})()");
                        }
                    });
                    holder.mWebView.loadUrl("file:///android_asset/wen(1).html");
                }
                isLoadHtml = true;
                GradientDrawable drawable = null;
                int color = -1;
                int icDocumentAgree = -1;
                if (mDocument != null) {
                    if (mDocument.getIsLike() == 1) {
                        icDocumentAgree = R.drawable.ic_document_like;
                        drawable = new GradientDrawable();
                        drawable.setCornerRadius(50);
                        color = ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary);
                        drawable.setStroke(2, ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
                    } else {
                        icDocumentAgree = R.drawable.ic_document_unlike;
                        drawable = new GradientDrawable();
                        drawable.setCornerRadius(50);
                        drawable.setStroke(2, ResourceUtils.getResourceColor(APP.getInstance(), R.color.grey500));
                        color = ResourceUtils.getResourceColor(APP.getInstance(), R.color.grey500);
                        holder.mLlLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mOnDocumentAgree != null && mDocument != null) {
                                    mOnDocumentAgree.onDocumentAgree(mDocument, holder.mTvAddOne, holder.mTvLikeCount,
                                            holder
                                                    .mIvLikeIcon, holder.mLlLike);
                                }
                            }
                        });
                    }
                    int likeCount = mDocument.getLikeCount();
                    holder.mIvLikeIcon.setImageResource(icDocumentAgree);
                    holder.mLlLike.setBackground(drawable);
                    holder.mTvLikeCount.setTextColor(color);
                    holder.mTvLikeCount.setText(String.valueOf(likeCount));
                }
            }
        }
    }

    private void onBindTitleViewHolder(ItemDocumentTitleHolder holder, int position) {
        if (mDocument != null) {
            if (mDocument.getIsGrap()!=2){
                holder.mTvDocumentTitle.requestFocus();
            }
            holder.mTvDocumentTitle.setText(mDocument.getTitle());
            holder.mTvAuthor.setText(mDocument.getSourceName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date parse = sdf.parse(mDocument.getShowTime());
                holder.mTvReleaseTime.setText(sdf.format(parse));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public int getItemTitleCount() {
        return 1;
    }

    public int getItemContentCount() {
        return 1;
    }

    public int getItemRecommendCount() {
        // TODO: 2017/8/16 有真实数据需要修改
        return 0;
    }

    public int getItemCommentCount() {
        return mComments.size();
    }

    public int getItemNoCommentCount() {
        if (mComments.size() == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return getItemTitleCount() + getItemContentCount() + getItemRecommendCount() + getItemCommentCount() +
                getItemNoCommentCount() + getCommentTabCount();
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
//            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>"+"<p></p>";
            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>";
            documentBody = documentBody.replace(oldChars, newChars);
        }
        LogUtils.e("TAG", documentBody);
        return documentBody;
    }

    // 注入js函数监听
    private void addImageClickListener(WebView webView) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imageClickListener.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    private void writeFile(String html) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/wen.txt");
        Log.i("TAG", file.getAbsolutePath());
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        DataOutputStream dos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(html.getBytes());
            Log.i("TAG", "write");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.i("TAG", "success");
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
