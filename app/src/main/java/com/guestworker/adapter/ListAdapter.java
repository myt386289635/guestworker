package com.guestworker.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.guestworker.R;
import com.guestworker.bean.ListBean;
import com.guestworker.databinding.ItemListBinding;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/15
 * @Describe
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ListBean.GoodsListBean> mList;
    private Context mContext;
    private OnItemClick mOnItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public ListAdapter(List<ListBean.GoodsListBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ItemListBinding binding  = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.item_list,viewGroup,false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.binding.setDate(mList.get(position));
        viewHolder.initTags(viewHolder.getBinding().itemFlex,position);
        viewHolder.itemView.setOnClickListener(v -> {
            if (mOnItemClick != null){
                mOnItemClick.onItemLayoutClick(position);
            }
        });

        // 立刻刷新界面
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemListBinding binding;

        public ViewHolder(@NonNull ItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemListBinding getBinding() {
            return binding;
        }

        public void initTags(FlexboxLayout flexboxLayout, int pos) {
            flexboxLayout.removeAllViews();
            for (int i = 0; i < 2; i++) {
                View itemTypeView = LayoutInflater.from(mContext).inflate(R.layout.item_goodstag, null);
                TextView textView = itemTypeView.findViewById(R.id.item_tag);
                textView.setText("多买优惠");
                flexboxLayout.addView(itemTypeView);
            }
        }
    }

    public interface OnItemClick{
        void onItemLayoutClick(int position);
    }
}
