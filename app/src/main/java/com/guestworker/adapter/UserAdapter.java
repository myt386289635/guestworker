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
import com.guestworker.bean.MyUserBean;
import com.guestworker.databinding.ItemUserBinding;
import com.guestworker.util.glide.GlideApp;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/18
 * @Describe
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private List<MyUserBean.SalesMemberListBean> mList;
    private Context mContext;
    private OnItemClick mOnItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public UserAdapter(List<MyUserBean.SalesMemberListBean> list, Context context) {
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

        viewHolder.mBinding.itemSelect.setVisibility(View.GONE);
        viewHolder.mBinding.itemName.setText(mList.get(position).getUsername());
        viewHolder.mBinding.itemPhone.setText(mList.get(position).getMobile());
        if (TextUtils.isEmpty(mList.get(position).getUserheadpath())){
            GlideApp.loderCircleImage(mContext,R.mipmap.default_img,viewHolder.mBinding.itemImage,R.mipmap.default_img,0);
        }else {
            GlideApp.loderCircleImage(mContext,mList.get(position).getUserheadpath(),viewHolder.mBinding.itemImage,R.mipmap.default_img,0);
        }
        viewHolder.itemView.setOnClickListener(v -> {
            if (mOnItemClick != null){
                mOnItemClick.onItem(position);
            }
        });
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
        void onItem(int position);
    }
}
