package com.guestworker.ui.activity.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.guestworker.R;
import com.guestworker.adapter.UserAdapter;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.MyUserBean;
import com.guestworker.databinding.ActivityUserBinding;
import com.guestworker.util.ResolRefreshQue;
import com.guestworker.util.ToastUtil;
import com.guestworker.view.dialog.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/18
 * @Describe  我的客户
 */
public class UserActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener, UserView, UserAdapter.OnItemClick {

    @Inject
    UserPresenter mPresenter;
    private ActivityUserBinding mBinding;
    private List<MyUserBean.SalesMemberListBean> mList;
    private UserAdapter mAdapter;
    private int page = 1;
    private Boolean refersh = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_user);
        mBinding.setTag(getIntent().getBooleanExtra("isMineModel",true));
        mBinding.setListener(this);
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        initView();
    }

    private void initView() {
        mBinding.inClude.titleTv.setText(getIntent().getStringExtra("title"));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new UserAdapter(mList,this);
        mAdapter.setOnItemClick(this);
        mBinding.recyclerView.setAdapter(mAdapter);

        ResolRefreshQue.solveScoll(mBinding.recyclerView,mBinding.swipeToLoad);
        mBinding.swipeToLoad.setOnRefreshListener(this);
        mBinding.swipeToLoad.setOnLoadMoreListener(this);
        mBinding.swipeToLoad.setRefreshing(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
        }
    }


    @Override
    public void onRefresh() {
        page = 1;
        refersh = true;
        mPresenter.getMyMember(this,page + "",this.bindToLifecycle());
    }

    @Override
    public void onLoadMore() {
        page++;
        refersh = false;
        mPresenter.getMyMember(this,page + "",this.bindToLifecycle());
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

    @Override
    public void onSuccess(MyUserBean myUserBean) {
        if(refersh){
            dissRefresh();
        }else {
            dissLoading();
        }
        if (myUserBean.getSalesMemberList() != null && myUserBean.getSalesMemberList().size() != 0 && myUserBean.getPage().getPagecount() >= page){
            //有数据
            mBinding.netClude.errorContainer.setVisibility(View.GONE);
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            if(refersh){
                mList.clear();
            }
            mList.addAll(myUserBean.getSalesMemberList());
            mAdapter.notifyDataSetChanged();
            if(!refersh){
                mBinding.swipeToLoad.setLoadMoreEnabled(false);
            }
        }else {
            //没有数据
            if(refersh){
                //刷新时
                initError();
            }else {
                //加载时
                page--;
                ToastUtil.show("已经是最后一页了");
            }
        }
    }

    @Override
    public void onFile(String error) {
        if(refersh){
            dissRefresh();
            initError();
        }else {
            dissLoading();
            page--;
        }
    }

    /**
     * 点击item
     */
    @Override
    public void onItem(int position) {
        DialogUtil.LoginDialog(this, "“客工”想打开您的电话", "确定", "取消", v -> diallPhone(mList.get(position).getMobile()));
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     * 该功能不需要动态申请权限的
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
