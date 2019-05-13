package com.guestworker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guestworker.R;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/5/7
 * @Describe
 */
public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder>{

    private List<String> mList;
    private Context mContext;

    public DiscountAdapter(List<String> list, Context context) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_discount,viewGroup,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
