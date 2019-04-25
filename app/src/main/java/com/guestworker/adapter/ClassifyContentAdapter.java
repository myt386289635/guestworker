package com.guestworker.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guestworker.R;
import com.guestworker.bean.ClassifyBean;
import com.guestworker.netwrok.RetrofitModule;
import com.guestworker.util.WeakRefHandler;
import com.guestworker.util.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/12
 * @Describe
 */
public class ClassifyContentAdapter extends RecyclerView.Adapter{

    private static final int HEAD = 1;
    private static final int CONTENT = 2;

    private List<ClassifyBean.ChildTypesBean> mClassifyContent;
    private List<String> mBannerList;
    private Context mContext;
    private Boolean isFlag;
    private OnItemClick mOnItemClick;
    private String name = "";

    public void setName(String name) {
        this.name = name;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public ClassifyContentAdapter(List<ClassifyBean.ChildTypesBean> classifyContent, Context context) {
        mClassifyContent = classifyContent;
        mContext = context;
        this.isFlag = true;
    }

    public void setBannerList(List<String> bannerList) {
        mBannerList = bannerList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return HEAD;
        }else {
            return CONTENT;
        }
    }

    /**
     * 为RecyclerView添加头布局
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case HEAD:
                View head = LayoutInflater.from(mContext).inflate(R.layout.item_classify_head, viewGroup, false);
                HeadViewHolder headViewHolder = new HeadViewHolder(head);
                return headViewHolder;
            case CONTENT:
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_classify_content, viewGroup, false);
                ContentViewHolder viewHolder = new ContentViewHolder(view);
                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case HEAD:
                HeadViewHolder headViewHolder = (HeadViewHolder) holder;

                headViewHolder.initBanner(mBannerList, mContext,headViewHolder.point
                        , headViewHolder.detail_point, headViewHolder.mPagerAdapter,headViewHolder.view_pager);
                if (mBannerList.size() == 1){
                    headViewHolder.detail_point.setVisibility(View.GONE);
                }else {
                    headViewHolder.detail_point.setVisibility(View.VISIBLE);
                    if (isFlag) {
                        new Thread(() -> {
                            while (true) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                headViewHolder.handler.sendEmptyMessage(100);
                            }
                        }).start();
                        isFlag = false;
                    }
                }
                headViewHolder.head_title.setText(name);

                break;
            case CONTENT:
                ContentViewHolder viewHolder = (ContentViewHolder) holder;
                int pos = position - 1;
                viewHolder.itemView.setOnClickListener(v -> {
                    if (mOnItemClick != null){
                        mOnItemClick.onItemLayoutClick(pos);
                    }
                });
                GlideApp.loderImage(mContext,RetrofitModule.IMG_URL + mClassifyContent.get(pos).getThumbnail(),viewHolder.item_image,0,0);
                viewHolder.item_name.setText(mClassifyContent.get(pos).getGtname());

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mClassifyContent.size()  == 0 ? 0 : mClassifyContent.size() + 1;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        private ViewPager view_pager;
        private LinearLayout detail_point;
        private TextView head_title;

        private ClassifyPagerAdapter mPagerAdapter;
        private List<ImageView> point;

        private Handler.Callback mCallback = msg -> {
            if(msg.what == 100){
                if (view_pager != null) {
                    view_pager.setCurrentItem(view_pager.getCurrentItem() + 1,true);
                }
            }
            return true;
        };
        private Handler handler = new WeakRefHandler(mCallback, Looper.getMainLooper());

        public HeadViewHolder(View itemView) {
            super(itemView);
            view_pager = itemView.findViewById(R.id.view_pager);
            detail_point = itemView.findViewById(R.id.detail_point);
            head_title  = itemView.findViewById(R.id.head_title);

//            if (headViewHolder.mPagerAdapter  != null){
//                headViewHolder.mPagerAdapter = null;
//            }
            mPagerAdapter = new ClassifyPagerAdapter(mContext);
            point = new ArrayList<>();
            mPagerAdapter.setList(mBannerList);
            mPagerAdapter.setPoint(point);
            mPagerAdapter.setViewPager(view_pager);
            view_pager.setAdapter(mPagerAdapter);
        }

        public void initBanner(List<String> mBannerList , Context context ,
                               List<ImageView> point, LinearLayout mDetailPoint,
                               ClassifyPagerAdapter mBannerAdapter,ViewPager view_pager){
            point.clear();
            mDetailPoint.removeAllViews();
            for (int i = 0; i < mBannerList.size(); i++) {
                ImageView imageView = new ImageView(context);
                point.add(imageView);
                if (i == 0) {
                    imageView.setImageResource(R.drawable.banner_down);
                } else {
                    imageView.setImageResource(R.drawable.banner_up);
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin =context.getResources().getDimensionPixelSize(R.dimen.x4);
                layoutParams.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.x4);
                mDetailPoint.addView(imageView, layoutParams);
            }
            mBannerAdapter.notifyDataSetChanged();
            view_pager.setCurrentItem(0,false);
        }
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_image;
        private TextView item_name;

        public ContentViewHolder(View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
        }
    }

    public interface OnItemClick{
        void onItemLayoutClick(int position);
    }
}
