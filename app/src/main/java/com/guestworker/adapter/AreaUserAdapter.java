package com.guestworker.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guestworker.R;
import com.guestworker.bean.UserBean;
import com.guestworker.databinding.ItemUserBinding;
import com.guestworker.util.glide.GlideApp;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class AreaUserAdapter extends RecyclerView.Adapter<AreaUserAdapter.ViewHolder> {

    private List<UserBean> mList;
    private Context mContext;
    private OnItemClick mOnItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public AreaUserAdapter(List<UserBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemUserBinding binding= DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.item_user,viewGroup,false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        viewHolder.mBinding.itemSelect.setVisibility(View.VISIBLE);
        if (mList.get(position).getTag()) {
            viewHolder.mBinding.itemSelect.setImageResource(R.mipmap.sel_user);
        } else {
            viewHolder.mBinding.itemSelect.setImageResource(R.mipmap.nor_user);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            if (mOnItemClick != null){
                mOnItemClick.onItemClick(position);
            }
        });

        viewHolder.mBinding.itemName.setText(mList.get(position).getBean().getUsername());
        viewHolder.mBinding.itemPhone.setText(mList.get(position).getBean().getMobile());
        if (TextUtils.isEmpty(mList.get(position).getBean().getUserheadpath())){
            GlideApp.loderCircleImage(mContext,R.mipmap.default_head,viewHolder.mBinding.itemImage,0,0);
        }else {
            GlideApp.loderCircleImage(mContext,mList.get(position).getBean().getUserheadpath(),viewHolder.mBinding.itemImage,R.mipmap.default_head,0);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemUserBinding mBinding;

        public ViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface OnItemClick{
        void onItemClick(int position);
    }
}
