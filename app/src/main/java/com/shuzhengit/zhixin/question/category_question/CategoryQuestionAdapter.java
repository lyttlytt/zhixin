//package com.shuzhengit.zhixin.question.category_question;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.shuzhengit.zhixin.APP;
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.bean.Question;
//import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
//import com.shuzhengit.zhixin.util.ResourceUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/9/27 14:14
// * E-mail:yuancongbin@gmail.com
// * <p>
// * 功能描述:
// */
//
//public class CategoryQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private Context mContext;
//    //无图
//    private static final int TYPE_ONE = 0;
//    //有图 1张图
//    private static final int TYPE_TWO = 1;
//    //有图 3张图
//    private static final int TYPE_THREE = 2;
//    private List<Question> data = new ArrayList<>();
//
//    public interface OnItemClickListener{
//        void onItemClick(Question question);
//    }
//    private OnItemClickListener mOnItemClickListener=null;
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        mOnItemClickListener = onItemClickListener;
//    }
//
//    public CategoryQuestionAdapter(Context context) {
//        mContext = context;
//    }
//
//    public void addMore(List<Question> data){
//        this.data.addAll(data);
//        notifyDataSetChanged();
//    }
//
//    public void refresh(List<Question> data){
//        this.data.clear();
//        this.data.addAll(data);
//        notifyDataSetChanged();
//    }
//    @Override
//    public int getItemViewType(int position) {
//        if (data.get(position).getImageType() == TYPE_THREE) {
//            return TYPE_THREE;
//        } else if (data.get(position).getImageType() == TYPE_TWO) {
//            return TYPE_TWO;
//        } else {
//            return TYPE_ONE;
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case TYPE_THREE:
//                return onCreateTypeThreeViewHolder(parent, viewType);
//            case TYPE_TWO:
//                return onCreateTypeTwoViewHolder(parent, viewType);
//            case TYPE_ONE:
//            default:
//                return onCreateTypeOneViewHolder(parent, viewType);
//        }
//    }
//
//    private TypeThreeViewHolder onCreateTypeThreeViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_type_three,parent,false);
//        return new TypeThreeViewHolder(view);
//    }
//
//    private TypeOneViewHolder onCreateTypeOneViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_type_one,parent,false);
//        return new TypeOneViewHolder(view);
//    }
//
//    private TypeTwoViewHolder onCreateTypeTwoViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_type_two,parent,false);
//        return new TypeTwoViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        int itemViewType = getItemViewType(position);
//        switch (itemViewType) {
//            case TYPE_THREE:
//                onBindTypeThreeViewHolder((TypeThreeViewHolder) holder, position);
//                break;
//            case TYPE_TWO:
//                onBindTypeTwoViewHolder((TypeTwoViewHolder) holder, position);
//                break;
//            case TYPE_ONE:
//            default:
//                onBindTypeOneViewHolder((TypeOneViewHolder) holder, position);
//                break;
//        }
//    }
//
//    private void onBindTypeThreeViewHolder(TypeThreeViewHolder holder, int position) {
//        Question question = data.get(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickListener!=null){
//                    mOnItemClickListener.onItemClick(question);
//                }
//            }
//        });
//        holder.tvQuestionTitle.setText(question.getTitle());
//        holder.tvAnswerCount.setText(String.format(ResourceUtils.getResourceString(APP.getInstance(),R.string
//                .questionAnswerCount),question.getAnswerCount()));
//
//        for (int i = 0; i < holder.mImageViews.length; i++) {
//            if (i<question.getImages().size()) {
//                GlideLoadImageUtils.loadImg(mContext, question.getImages().get(i), holder.mImageViews[i], R.drawable
//                        .shape_img_placeholder);
//            }else {
//
//            }
//        }
//    }
//
//    private void onBindTypeOneViewHolder(TypeOneViewHolder holder, int position) {
//        Question question = data.get(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickListener!=null){
//                    mOnItemClickListener.onItemClick(question);
//                }
//            }
//        });
//        holder.tvQuestionTitle.setText(question.getTitle());
//        holder.tvAnswerCount.setText(String.format(ResourceUtils.getResourceString(APP.getInstance(),R.string
//                .questionAnswerCount),question.getAnswerCount()));
//    }
//
//    private void onBindTypeTwoViewHolder(TypeTwoViewHolder holder, int position) {
//        Question question = data.get(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickListener!=null){
//                    mOnItemClickListener.onItemClick(question);
//                }
//            }
//        });
//        holder.tvQuestionTitle.setText(question.getTitle());
//        holder.tvAnswerCount.setText(String.format(ResourceUtils.getResourceString(APP.getInstance(),R.string
//                .questionAnswerCount),question.getAnswerCount()));
//        GlideLoadImageUtils.loadImg(mContext,question.getImages().get(0),holder.ivBanner,R.drawable.shape_img_placeholder);
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    class TypeOneViewHolder extends RecyclerView.ViewHolder {
//        TextView tvQuestionTitle,tvAnswerCount;
//        public TypeOneViewHolder(View itemView) {
//            super(itemView);
//            tvQuestionTitle = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
//            tvAnswerCount = (TextView) itemView.findViewById(R.id.tvAnswerCount);
//        }
//    }
//
//    class TypeTwoViewHolder extends RecyclerView.ViewHolder {
//        TextView tvQuestionTitle,tvAnswerCount;
//        ImageView ivBanner;
//        public TypeTwoViewHolder(View itemView) {
//            super(itemView);
//            tvQuestionTitle = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
//            tvAnswerCount = (TextView) itemView.findViewById(R.id.tvAnswerCount);
//            ivBanner = (ImageView) itemView.findViewById(R.id.ivBanner);
//        }
//    }
//    class TypeThreeViewHolder extends RecyclerView.ViewHolder {
//        TextView tvQuestionTitle,tvAnswerCount;
//        ImageView[] mImageViews = new ImageView[3];
//        public TypeThreeViewHolder(View itemView) {
//            super(itemView);
//            tvQuestionTitle = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
//            tvAnswerCount = (TextView) itemView.findViewById(R.id.tvAnswerCount);
//            mImageViews[0] = (ImageView) itemView.findViewById(R.id.ivBanner1);
//            mImageViews[1] = (ImageView) itemView.findViewById(R.id.ivBanner2);
//            mImageViews[2] = (ImageView) itemView.findViewById(R.id.ivBanner3);
//        }
//    }
//}
