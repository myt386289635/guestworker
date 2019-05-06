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
import com.guestworker.bean.AreaUserBean;
import com.guestworker.bean.ShoppingCartBean;
import com.guestworker.databinding.ItemCartBinding;
import com.guestworker.databinding.ItemCartBottemBinding;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/20
 * @Describe
 */
public class CartAdapter extends RecyclerView.Adapter{

    private static final int CONTENT = 1;
    private static final int BOOTEM = 2;

    private List<ShoppingCartBean> mList;
    private Context mContext;
    private OnItemClick mOnItemClick;
    private Boolean isUser = false;//用户布局展示
    private AreaUserBean.AreaMemberListBean mMemberListBean;
    private String remark;//备注

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setMemberListBean(AreaUserBean.AreaMemberListBean memberListBean) {
        mMemberListBean = memberListBean;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public CartAdapter(List<ShoppingCartBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()){
            return BOOTEM;
        }else {
            return CONTENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        switch (i){
            case BOOTEM:
                ItemCartBottemBinding bottemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.item_cart_bottem, viewGroup ,false);
                holder = new ViewHolderBottom(bottemBinding);
                break;
            case CONTENT:
                ItemCartBinding itemCartBinding  = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.item_cart, viewGroup ,false);
                holder = new ViewHolderContent(itemCartBinding);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type){
            case CONTENT:
                ViewHolderContent holderContent = (ViewHolderContent) holder;
                holderContent.mItemCartBinding.setDate(mList.get(position).getCartBean().getBean().getDefaultgoods());
                holderContent.mItemCartBinding.setMun(mList.get(position).getCartBean());
                if (mList.get(position).getSelect()){
                    holderContent.mItemCartBinding.itemSelect.setImageResource(R.mipmap.cart_sel);
                }else {
                    holderContent.mItemCartBinding.itemSelect.setImageResource(R.mipmap.cart_nor);
                }
                holderContent.mItemCartBinding.confirmDecrease.setOnClickListener(v -> {
                    //减
                    if (mOnItemClick != null){
                        mOnItemClick.onDecrease(position);
                    }
                });
                holderContent.mItemCartBinding.confirmIncrease.setOnClickListener(v -> {
                    //加
                    if (mOnItemClick != null){
                        mOnItemClick.onIncrease(position);
                    }
                });
                holderContent.mItemCartBinding.itemSelect.setOnClickListener(v -> {
                    //选择
                    if (mOnItemClick != null){
                        mOnItemClick.onSelect(position);
                    }
                });

                // 立刻刷新界面
                holderContent.mItemCartBinding.executePendingBindings();
                break;
            case BOOTEM:
                ViewHolderBottom holderBottom = (ViewHolderBottom) holder;
                holderBottom.mBottemBinding.setUser(mMemberListBean);
                holderBottom.mBottemBinding.confirmConfirmUserContainer.setOnClickListener(v -> {
                    if (mOnItemClick != null){
                        mOnItemClick.onUserContainer();
                    }
                });
                holderBottom.mBottemBinding.confirmUserContainer.setOnClickListener(v -> {
                    if (mOnItemClick != null){
                        mOnItemClick.onUserContainer();
                    }
                });
                holderBottom.mBottemBinding.confirmDiscountContainer.setOnClickListener(v -> {
                    if (mOnItemClick != null){
                        mOnItemClick.onDiscount();
                    }
                });
                holderBottom.mBottemBinding.confirmRemarkContainer.setOnClickListener(v -> {
                    if (mOnItemClick != null){
                        mOnItemClick.onRemark(holderBottom.mBottemBinding.confirmRemark.getText().toString());
                    }
                });
                if (isUser){
                    holderBottom.mBottemBinding.confirmConfirmUserContainer.setVisibility(View.VISIBLE);
                    holderBottom.mBottemBinding.confirmUserContainer.setVisibility(View.GONE);
                }else {
                    holderBottom.mBottemBinding.confirmUserContainer.setVisibility(View.VISIBLE);
                    holderBottom.mBottemBinding.confirmConfirmUserContainer.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(remark)){
                    holderBottom.mBottemBinding.confirmRemark.setText("备注：" + remark);
                }else {
                    holderBottom.mBottemBinding.confirmRemark.setText("添加备注");
                }
                // 立刻刷新界面
                holderBottom.mBottemBinding.executePendingBindings();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size()  == 0 ? 0 : mList.size() + 1;
    }

    class ViewHolderBottom extends RecyclerView.ViewHolder{

        ItemCartBottemBinding mBottemBinding;

        public ViewHolderBottom(@NonNull ItemCartBottemBinding bottemBinding) {
            super(bottemBinding.getRoot());
            mBottemBinding = bottemBinding;
        }
    }

    class ViewHolderContent extends RecyclerView.ViewHolder{

        private ItemCartBinding mItemCartBinding;

        public ViewHolderContent(@NonNull ItemCartBinding itemCartBinding) {
            super(itemCartBinding.getRoot());
            mItemCartBinding = itemCartBinding;
        }
    }

    public interface OnItemClick{
        void onDecrease(int position);
        void onIncrease(int position);
        void onSelect(int position);
        void onUserContainer();
        void onRemark(String remark);
        void onDiscount();
    }
}
