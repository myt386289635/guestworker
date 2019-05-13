package com.guestworker.ui.activity.confirm.discount;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.guestworker.R;
import com.guestworker.adapter.DiscountAdapter;
import com.guestworker.base.BaseActivity;
import com.guestworker.databinding.ActivityUserBinding;
import com.guestworker.util.ResolRefreshQue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/5/7
 * @Describe 我的优惠券
 */
public class DiscountActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {

    @Inject
    DiscountPresenter mPresenter;
    private ActivityUserBinding mBinding;
    private List<String> mList;
    private DiscountAdapter mAdapter;
    private int page = 1;
    private Boolean refersh = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        mBinding.setTag(getIntent().getBooleanExtra("isMineModel",false));
        mBinding.setListener(this);
        mBaseActivityComponent.inject(this);
        initView();
    }

    private void initView() {
        mBinding.userSave.setText("不使用");
        mBinding.inClude.titleTv.setText("我的优惠券");

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new DiscountAdapter(mList,this);
        mBinding.recyclerView.setAdapter(mAdapter);

        ResolRefreshQue.solveScoll(mBinding.recyclerView,mBinding.swipeToLoad);
        mBinding.swipeToLoad.setOnRefreshListener(this);
        mBinding.swipeToLoad.setOnLoadMoreListener(this);
        mBinding.swipeToLoad.setRefreshing(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_save:
                //不使用

                break;
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public void dissRefresh(){
        if (mBinding.swipeToLoad != null && mBinding.swipeToLoad.isRefreshing()) {
            if (!mBinding.swipeToLoad.isRefreshEnabled()) {
                mBinding.swipeToLoad.setRefreshEnabled(true);
                mBinding.swipeToLoad.setRefreshing(false);
                mBinding.swipeToLoad.setRefreshEnabled(false);
            } else {
                mBinding.swipeToLoad.setRefreshing(false);
            }
        }
    }

    public void dissLoading(){
        if (mBinding.swipeToLoad != null && mBinding.swipeToLoad.isLoadingMore()) {
            if (!mBinding.swipeToLoad.isLoadMoreEnabled()) {
                mBinding.swipeToLoad.setLoadMoreEnabled(true);
                mBinding.swipeToLoad.setLoadingMore(false);
                mBinding.swipeToLoad.setLoadMoreEnabled(false);
            } else {
                mBinding.swipeToLoad.setLoadingMore(false);
            }
        }
    }

    public void initError() {
        mBinding.netClude.errorContainer.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }
}
