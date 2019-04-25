package com.guestworker.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guestworker.R;
import com.guestworker.bean.ClassifyTitleBean;
import com.guestworker.databinding.ItemClassifyTitleBinding;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/12
 * @Describe 分类title
 */
public class ClassifyTitleAdapter extends RecyclerView.Adapter<ClassifyTitleAdapter.ViewHolder>{

    private List<ClassifyTitleBean> mClassifyTitle;
    private Context mContext;
    private OnItemClick mOnItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public ClassifyTitleAdapter(List<ClassifyTitleBean> classifyTitle, Context context) {
        mClassifyTitle = classifyTitle;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemClassifyTitleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.item_classify_title,viewGroup,false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.itemView.setOnClickListener(v -> {
            if (mOnItemClick != null){
                mOnItemClick.onItemClick(viewHolder.binding.itemClassify, position);
            }
        });
        if(mClassifyTitle.get(position).getUp()){
            //选中
            viewHolder.binding.itemContainer.setBackgroundColor(mContext.getResources().getColor(R.color.color_white));
            viewHolder.binding.itemClassify.getPaint().setFakeBoldText(true);
            viewHolder.binding.itemClassify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.binding.itemTag.setVisibility(View.VISIBLE);
        }else {
            //没选中
            viewHolder.binding.itemContainer.setBackgroundColor(mContext.getResources().getColor(R.color.color_F6F6F6));
            viewHolder.binding.itemClassify.getPaint().setFakeBoldText(false);
            viewHolder.binding.itemClassify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            viewHolder.binding.itemTag.setVisibility(View.GONE);
        }
        viewHolder.binding.itemClassify.setText(mClassifyTitle.get(position).getTypesBean().getGtname());
//        // 立刻刷新界面
//        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mClassifyTitle.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemClassifyTitleBinding binding;

        public ViewHolder(@NonNull  ItemClassifyTitleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClick{
        void onItemClick(TextView textView ,int position);
    }
}
