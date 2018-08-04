package com.shuzhengit.zhixin.column.holder;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.column.holder.base.APPConst;
import com.shuzhengit.zhixin.column.holder.base.EditModeHandler;
import com.shuzhengit.zhixin.column.holder.base.IChannelType;
import com.shuzhengit.zhixin.column.holder.base.ItemDragHelperCallback;
import com.shuzhengit.zhixin.column.holder.base.ItemDragListener;
import com.shuzhengit.zhixin.column.holder.base.ItemDragVHListener;
import com.shuzhengit.zhixin.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import static com.shuzhengit.zhixin.column.holder.base.IChannelType.TYPE_MY_CHANNEL_HEADER;
import static com.shuzhengit.zhixin.column.holder.base.IChannelType.TYPE_REC_CHANNEL;
import static com.shuzhengit.zhixin.column.holder.base.IChannelType.TYPE_REC_CHANNEL_HEADER;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/27 14:24
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ColumnAdapter2 extends RecyclerView.Adapter<ColumnAdapter2.ChannelViewHolder> implements
        ItemDragListener {
    private List<Column> mMyChannelItems = new ArrayList<>();
    private List<Column> mOtherChannelItems = new ArrayList<>();
    private int mMyHeaderCount;
    private int mRecHeaderCount;
    private LayoutInflater mInflater;
    private SparseArray<IChannelType> mTypeMap = new SparseArray();
    private boolean isEditMode;
    private ItemTouchHelper mItemTouchHelper;
    // touch 点击开始时间
    private long startTime;
    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;
    private ChannelItemClickListener channelItemClickListener;
    private boolean isModify = false;
    private int isCurrentPositionId = -1;
    public boolean isModify() {
        return isModify;
    }

//    public ColumnAdapter2(Context context, RecyclerView recyclerView, List<Column> myChannelItems,
//                          List<Column> otherChannelItems,
//                          int myHeaderCount, int recHeaderCount) {
//        this.mItemTouchHelper = new ItemTouchHelper(new ItemDragHelperCallback(this));
//        mItemTouchHelper.attachToRecyclerView(recyclerView);
//        this.mMyChannelItems = myChannelItems;
//        this.mOtherChannelItems = otherChannelItems;
//        this.mMyHeaderCount = myHeaderCount;
//        this.mRecHeaderCount = recHeaderCount;
//        mInflater = LayoutInflater.from(context);
//        mTypeMap.put(TYPE_MY_CHANNEL_HEADER, new MyChannelHeaderWidget(new EditHandler()));
//        mTypeMap.put(IChannelType.TYPE_MY_CHANNEL, new MyChannelWidget(new EditHandler()));
//        mTypeMap.put(TYPE_REC_CHANNEL_HEADER, new RecChannelHeaderWidget());
//        mTypeMap.put(TYPE_REC_CHANNEL, new RecChannelWidget(new EditHandler()));
//    }

    public ColumnAdapter2(Context context, RecyclerView recyclerView,
                          int myHeaderCount, int recHeaderCount,int isCurrentPositionId) {
        this.mItemTouchHelper = new ItemTouchHelper(new ItemDragHelperCallback(this));
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        this.mMyHeaderCount = myHeaderCount;
        this.mRecHeaderCount = recHeaderCount;
        this.isCurrentPositionId = isCurrentPositionId;
        mInflater = LayoutInflater.from(context);
        mTypeMap.put(TYPE_MY_CHANNEL_HEADER, new MyChannelHeaderWidget(new EditHandler()));
        mTypeMap.put(IChannelType.TYPE_MY_CHANNEL, new MyChannelWidget(new EditHandler()));
        mTypeMap.put(TYPE_REC_CHANNEL_HEADER, new RecChannelHeaderWidget());
        mTypeMap.put(TYPE_REC_CHANNEL, new RecChannelWidget(new EditHandler()));
    }

    public List<Column> getMyChannelItems() {
        return mMyChannelItems;
    }

    public void setMyChannelItems(List<Column> myChannelItems) {
        mMyChannelItems = myChannelItems;
        notifyDataSetChanged();
    }

    public void setOtherChannelItems(List<Column> otherChannelItems) {
        mOtherChannelItems = otherChannelItems;
        notifyDataSetChanged();
    }
    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mTypeMap.get(viewType).createViewHolder(mInflater, parent);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        if (getItemViewType(position) == IChannelType.TYPE_MY_CHANNEL) {
            int myPosition = position - mMyHeaderCount;
            myPosition = myPosition < 0 || myPosition >= mMyChannelItems.size() ? 0 : myPosition;
            mTypeMap.get(getItemViewType(position)).bindViewHolder(holder, position, mMyChannelItems.get(myPosition));
            return;
        }
        if (getItemViewType(position) == TYPE_REC_CHANNEL) {
            int otherPosition = position - mMyChannelItems.size() - mMyHeaderCount - mRecHeaderCount;
            otherPosition = otherPosition < 0 || otherPosition >= mOtherChannelItems.size() ? 0 : otherPosition;
            mTypeMap.get(getItemViewType(position)).bindViewHolder(holder, position, mOtherChannelItems.get
                    (otherPosition));
            return;
        }
        mTypeMap.get(getItemViewType(position)).bindViewHolder(holder, position, null);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mMyHeaderCount) {
            return TYPE_MY_CHANNEL_HEADER;
        }
        if (position >= mMyHeaderCount && position < mMyChannelItems.size() + mMyHeaderCount) {
            return IChannelType.TYPE_MY_CHANNEL;
        }
        if (position >= mMyChannelItems.size() + mMyHeaderCount && position < mMyChannelItems.size() + mMyHeaderCount
                + mRecHeaderCount) {
            return TYPE_REC_CHANNEL_HEADER;
        }
        return TYPE_REC_CHANNEL;
    }

    @Override
    public int getItemCount() {
        return mMyChannelItems.size() + mOtherChannelItems.size() + mMyHeaderCount + mRecHeaderCount;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (toPosition > 2) {
            Column item = mMyChannelItems.get(fromPosition - mMyHeaderCount);
            mMyChannelItems.remove(fromPosition - mMyHeaderCount);
            mMyChannelItems.add(toPosition - mMyHeaderCount, item);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemSwiped(int position) {
    }

    public static class ChannelViewHolder extends RecyclerView.ViewHolder implements ItemDragVHListener {

        public ChannelViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onItemSelected() {
            scaleItem(1.0f, 1.2f, 0.5f);
        }

        @Override
        public void onItemFinished() {
            scaleItem(1.2f, 1.0f, 1.0f);
        }

        public void scaleItem(float start, float end, float alpha) {
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(itemView, "scaleX",
                    start, end);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(itemView, "scaleY",
                    start, end);
            ObjectAnimator anim3 = ObjectAnimator.ofFloat(itemView, "alpha",
                    alpha);

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(200);
            animSet.setInterpolator(new LinearInterpolator());
            animSet.playTogether(anim1, anim2, anim3);
            animSet.start();
        }
    }

    private class EditHandler extends EditModeHandler {
        @Override
        public void startEditMode(RecyclerView mRecyclerView) {
            doStartEditMode(mRecyclerView);
        }

        @Override
        public void cancelEditMode(RecyclerView mRecyclerView) {
            doCancelEditMode(mRecyclerView);
        }

        @Override
        public void clickMyChannel(RecyclerView mRecyclerView, ChannelViewHolder holder,boolean isEdit) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            int position = holder.getAdapterPosition();
            if (isEditMode ) {
                if (!isEdit){
                    return;
                }
                View targetView = layoutManager.findViewByPosition(mMyChannelItems.size()
                        + mMyHeaderCount + mRecHeaderCount);
                View currentView = mRecyclerView.getLayoutManager().findViewByPosition(position);
                int targetX = 0;
                int targetY = 0;
                if (mRecyclerView.indexOfChild(targetView) >= 0) {
                    int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
                    targetX = targetView.getLeft();
                    targetY = targetView.getTop();
                    if ((mMyChannelItems.size()) % spanCount == 1) {
                        View preTargetView = layoutManager.findViewByPosition(mMyChannelItems.size()
                                + mMyHeaderCount + mRecHeaderCount - 1);
                        targetX = preTargetView.getLeft();
                        targetY = preTargetView.getTop();
                    }
                } else {
                    View preTargetView = layoutManager.findViewByPosition(mMyChannelItems.size()
                            + mMyHeaderCount + mRecHeaderCount - 1);
                    if (preTargetView != null) {
                        targetX = preTargetView.getLeft();
                        targetY = preTargetView.getTop() + preTargetView.getHeight() + APPConst.ITEM_SPACE;
                    }
                }
                moveMyToOther(position);
                startAnimation(mRecyclerView, currentView, targetX, targetY);
            } else {
                if (channelItemClickListener != null) {
                    channelItemClickListener.onChannelItemClick(mMyChannelItems, position - mMyHeaderCount);
                }
            }
        }

        @Override
        public void touchMyChannel(MotionEvent motionEvent, ChannelViewHolder holder) {
            if (!isEditMode) {
                return;
            }
            switch (MotionEventCompat.getActionMasked(motionEvent)) {
                case MotionEvent.ACTION_DOWN:
                    startTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                        mItemTouchHelper.startDrag(holder);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    startTime = 0;
                    break;
                default:
                    break;
            }
        }

        @Override
        public void clickLongMyChannel(RecyclerView mRecyclerView, ChannelViewHolder holder) {
            if (!isEditMode) {
                doStartEditMode(mRecyclerView);
                View view = mRecyclerView.getChildAt(0);
                if (view == mRecyclerView.getLayoutManager().findViewByPosition(0)) {
                    TextView dragTip = (TextView) view.findViewById(R.id.id_my_header_tip_tv);
                    dragTip.setText("拖拽可以排序");

                    TextView tvBtnEdit = (TextView) view.findViewById(R.id.id_edit_mode);
                    tvBtnEdit.setText("完成");
                    tvBtnEdit.setSelected(true);
                }
                mItemTouchHelper.startDrag(holder);
                isModify = true;
            }
        }

        @Override
        public void clickRecChannel(RecyclerView mRecyclerView, ChannelViewHolder holder) {
            GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            int position = holder.getAdapterPosition();
            View targetView = layoutManager.findViewByPosition(mMyChannelItems.size() + mMyHeaderCount - 1);
            View currentView = mRecyclerView.getLayoutManager().findViewByPosition(position);
            if (mRecyclerView.indexOfChild(targetView) >= 0) {
                int targetX = targetView.getLeft();
                int targetY = targetView.getTop();
                int spanCount = layoutManager.getSpanCount();
                View nextTargetView = layoutManager.findViewByPosition(mMyChannelItems.size() + mMyHeaderCount);
                if (mMyChannelItems.size() % spanCount == 0) {
                    targetX = nextTargetView.getLeft();
                    targetY = nextTargetView.getTop();
                } else {
                    targetX += targetView.getWidth() + 2 * APPConst.ITEM_SPACE;
                }
                moveOtherToMy(position);
                startAnimation(mRecyclerView, currentView, targetX, targetY);
            } else {
                moveOtherToMy(position);
            }
        }
    }

    /**
     * 开启编辑模式
     *
     * @param parent
     */
    private void doStartEditMode(RecyclerView parent) {
        isEditMode = true;
        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.id_delete_icon);
            if (imgEdit != null) {
                Column item = mMyChannelItems.get(i - mMyHeaderCount);
                //通过column的type设置文字背景贪色
                int type = item.getModifiable();
                if (type == APPConst.ITEM_EDIT) {
                    imgEdit.setVisibility(View.VISIBLE);
                } else {
                    imgEdit.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 取消编辑模式
     *
     * @param parent
     */
    public void doCancelEditMode(RecyclerView parent) {
        isEditMode = false;
        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.id_delete_icon);
            if (imgEdit != null) {
                imgEdit.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 将我的频道的内容移动到更多频道中
     *
     * @param position
     */
    private void moveMyToOther(int position) {
        isModify = true;
        int myPosition = position - mMyHeaderCount;
        Column item = mMyChannelItems.get(myPosition);
        mMyChannelItems.remove(myPosition);
        mOtherChannelItems.add(0, item);
        notifyItemMoved(position, mMyChannelItems.size() + mMyHeaderCount + mRecHeaderCount);
    }

    /**
     * 将更多频道的内容移动到我的频道中
     *
     * @param position
     */
    private void moveOtherToMy(int position) {
        isModify = true;
        int recPosition = processItemRemoveAdd(position);
        if (recPosition == -1) {
            return;
        }
        notifyItemMoved(position, mMyChannelItems.size() + mMyHeaderCount - 1);
    }

    private int processItemRemoveAdd(int position) {
        int startPosition = position - mMyChannelItems.size() - mRecHeaderCount - mMyHeaderCount;
        if (startPosition > mOtherChannelItems.size() - 1) {
            return -1;
        }
        Column item = mOtherChannelItems.get(startPosition);
        // TODO: 2017/10/27 栏目的状态
//        item.setEditStatus(isEditMode ? 1 : 0);
        mOtherChannelItems.remove(startPosition);
        mMyChannelItems.add(item);
        return position;
    }

    private void startAnimation(RecyclerView recyclerView, final View currentView, float targetX, float targetY) {
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);
        Animation animation = getTranslateAnimator(targetX - currentView.getLeft(),
                targetY - currentView.getTop());
        currentView.setVisibility(View.INVISIBLE);
        mirrorView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(mirrorView);
                if (currentView.getVisibility() == View.INVISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {
        /**
         * 我们要获取cache首先要通过setDrawingCacheEnable方法开启cache，然后再调用getDrawingCache方法就可以获得view的cache图片了。
         buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。
         若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
         当调用setDrawingCacheEnabled方法设置为false, 系统也会自动把原来的cache销毁。
         */
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView = new ImageView(recyclerView.getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int[] parenLocations = new int[2];
        parent.getLocationOnScreen(parenLocations);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);
        return mirrorView;
    }

    private TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(360);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    public void setChannelItemClickListener(ChannelItemClickListener channelItemClickListener) {
        this.channelItemClickListener = channelItemClickListener;
    }

    public interface ChannelItemClickListener {
        void onChannelItemClick(List<Column> list, int position);
    }
    class MyChannelHeaderWidget implements IChannelType {
        private RecyclerView mRecyclerView;
        private EditModeHandler editModeHandler;

        public MyChannelHeaderWidget(EditModeHandler handler) {
            this.editModeHandler = handler;
        }

        @Override
        public ColumnAdapter2.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
            mRecyclerView = (RecyclerView) parent;
            return new MyChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_my_header, parent, false));
        }

        @Override
        public void bindViewHolder(final ColumnAdapter2.ChannelViewHolder holder, int position, Column data) {
            final MyChannelHeaderViewHolder viewHolder = (MyChannelHeaderViewHolder) holder;
            // 右侧按键点击时改变样式，如编辑-》完成
            viewHolder.mEditModeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!viewHolder.mEditModeTv.isSelected()) {

                        if (editModeHandler != null) {
                            editModeHandler.startEditMode(mRecyclerView);
                        }
                        viewHolder.mEditModeTv.setText("完成");
                    } else {
                        if (editModeHandler != null) {
                            editModeHandler.cancelEditMode(mRecyclerView);
                        }
                        viewHolder.mEditModeTv.setText("编辑");
                    }
                    viewHolder.mEditModeTv.setSelected(!viewHolder.mEditModeTv.isSelected());
                }
            });
        }

        public class MyChannelHeaderViewHolder extends ColumnAdapter2.ChannelViewHolder {
            private TextView mEditModeTv;

            public MyChannelHeaderViewHolder(View itemView) {
                super(itemView);
                mEditModeTv = (TextView) itemView.findViewById(R.id.id_edit_mode);
            }
        }
    }


    class MyChannelWidget implements IChannelType {
        private RecyclerView mRecyclerView;
        private EditModeHandler editModeHandler;

        public MyChannelWidget(EditModeHandler editModeHandler) {
            this.editModeHandler = editModeHandler;
        }

        @Override
        public ColumnAdapter2.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
            mRecyclerView = (RecyclerView) parent;
            return new MyChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_my, parent, false));
        }

        @Override
        public void bindViewHolder(final ColumnAdapter2.ChannelViewHolder holder, final int position, final Column data) {
            final MyChannelHeaderViewHolder myHolder = (MyChannelHeaderViewHolder) holder;
            myHolder.mChannelTitleTv.setText(data.getColumnTitle());
            // 设置文字大小，通过判断tab中的文字长度，如果有4或者4个字以上则为16sp大小
            int textSize = data.getColumnTitle().length() > 4 ? 12 : 14;
            myHolder.mChannelTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            // 通过channelbean的type值设置其文字背景颜色
            int type = data.getModifiable();
            // 设置文字背景颜色
//        myHolder.mChannelTitleTv.setBackgroundResource(type == Constant.ITEM_DEFAULT || type == APPConst.ITEM_UNEDIT ?
//                R.drawable.channel_fixed_bg_shape : R.drawable.channel_my_bg_shape);
            //设置文字背景颜色
            myHolder.mChannelTitleTv.setBackgroundResource(type == 0 ? R.drawable.shape_column_bg : R.drawable
                    .shape_column_bg);


            // 根据tab状态设置文字颜色
//            myHolder.mChannelTitleTv.setTextColor(type == APPConst.ITEM_DEFAULT ? ResourceUtils.getResourceColor(APP
//                    .getInstance(),R.color.grey500) :
//                    type == APPConst.ITEM_UNEDIT ? Color.parseColor("#666666") : Color.parseColor("#333333"));
            if (data.getId()==isCurrentPositionId){
                myHolder.mChannelTitleTv.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.red600));
            }
            // 设置右上角删除按钮是否可见
            // TODO: 2017/10/27 栏目的状态
//            if (isEditMode){
                myHolder.mDeleteIv.setVisibility(isEditMode?View.VISIBLE:View.INVISIBLE);
//            }
//        myHolder.mDeleteIv.setVisibility(data.getEditStatus() == 1 ? View.VISIBLE : View.INVISIBLE);
            myHolder.mChannelTitleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (editModeHandler != null && data.getModifiable() == APPConst.ITEM_EDIT) {
                    if (editModeHandler != null) {
                        editModeHandler.clickMyChannel(mRecyclerView, holder,data.getModifiable()==APPConst.ITEM_EDIT);
                    }
                    else {
                        editModeHandler.clickMyChannel(mRecyclerView, holder,data.getModifiable()==APPConst.ITEM_DEFAULT);
                    }
                }
            });
            myHolder.mChannelTitleTv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (editModeHandler != null && data.getModifiable() == APPConst.ITEM_EDIT) {
                        editModeHandler.touchMyChannel(motionEvent, holder);
                    }
                    else{
                        editModeHandler.touchMyChannel(motionEvent, holder);
                    }
                    return false;
                }
            });
            myHolder.mChannelTitleTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (editModeHandler != null && data.getModifiable() == APPConst.ITEM_EDIT) {
                        editModeHandler.clickLongMyChannel(mRecyclerView, holder);
                    }
                    else {
                        editModeHandler.clickLongMyChannel(mRecyclerView, holder);
                    }
                    return true;
                }
            });
        }

        public class MyChannelHeaderViewHolder extends ColumnAdapter2.ChannelViewHolder {
            private TextView mChannelTitleTv;
            private ImageView mDeleteIv;

            private MyChannelHeaderViewHolder(View itemView) {
                super(itemView);
                mChannelTitleTv = (TextView) itemView.findViewById(R.id.id_channel_title);
                mDeleteIv = (ImageView) itemView.findViewById(R.id.id_delete_icon);
            }
        }
    }

    class RecChannelHeaderWidget implements IChannelType {
        @Override
        public ColumnAdapter2.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
            return new MyChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_rec_header, parent, false));
        }

        @Override
        public void bindViewHolder(ColumnAdapter2.ChannelViewHolder holder, int position, Column data) {

        }

        public class MyChannelHeaderViewHolder extends ColumnAdapter2.ChannelViewHolder {

            public MyChannelHeaderViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


    class RecChannelWidget implements IChannelType {
        private EditModeHandler editModeHandler;
        private RecyclerView mRecyclerView;

        public RecChannelWidget(EditModeHandler editModeHandler) {
            this.editModeHandler = editModeHandler;
        }

        @Override
        public ColumnAdapter2.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
            this.mRecyclerView = (RecyclerView) parent;
            return new RecChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_rec, parent, false));
        }

        @Override
        public void bindViewHolder(final ColumnAdapter2.ChannelViewHolder holder, int position, Column data) {
            RecChannelHeaderViewHolder recHolder = (RecChannelHeaderViewHolder) holder;
            recHolder.mChannelTitleTv.setText(data.getColumnTitle());
            int textSize = data.getColumnTitle().length() > 4 ? 12 : 14;
            recHolder.mChannelTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            recHolder.mChannelTitleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editModeHandler != null) {
                        editModeHandler.clickRecChannel(mRecyclerView, holder);
                    }
                }
            });
        }

        private class RecChannelHeaderViewHolder extends ColumnAdapter2.ChannelViewHolder {
            private TextView mChannelTitleTv;

            private RecChannelHeaderViewHolder(View itemView) {
                super(itemView);
                mChannelTitleTv = (TextView) itemView.findViewById(R.id.id_channel_title);
            }
        }
    }

}
