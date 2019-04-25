package com.guestworker.ui.activity.shoplist;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.guestworker.R;
import com.guestworker.adapter.ListAdapter;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.ListBean;
import com.guestworker.bean.eventbus.CartpageBus;
import com.guestworker.databinding.ActivityListBinding;
import com.guestworker.ui.activity.detail.DetailActivity;
import com.guestworker.util.ClickUtil;
import com.guestworker.util.ResolRefreshQue;
import com.guestworker.util.ToastUtil;
import com.guestworker.view.dialog.ProgressDialogView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/15
 * @Describe 商品列表
 */
public class ListActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener, ListAdapter.OnItemClick, ListView {

    @Inject
    ListPresenter mPresenter;

    private ActivityListBinding mBinding;
    private int currentPosition = 0;//当前选中的位置 默认是0
    private List<TextView> mTextViews;
    /**
     * 页数
     */
    private int page = 1;
    /**
     * 记录是刷新还是加载
     */
    private Boolean refersh = true;
    private ListAdapter mAdapter;
    private List<ListBean.GoodsListBean> mList;
    private int sort = 1;//0：升序  1：降序
    private Dialog mDialog;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_list);
        mBinding.setListener(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mBaseActivityComponent.inject(this);
        mBinding.inClude.titleTv.setText(getIntent().getStringExtra("title"));
        mBinding.upBlack.setColorFilter(Color.BLACK);
        mBinding.downBlack.setColorFilter(Color.BLACK);
        id = getIntent().getStringExtra("gtid");
        mPresenter.setView(this);

        mTextViews = new ArrayList<>();
        mTextViews.add(mBinding.titleComprehensive);
        mTextViews.add(mBinding.titleSalesVolume);
        mTextViews.add(mBinding.titleNew);
        mTextViews.add(mBinding.itemPrice);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new ListAdapter(mList,this);
        mAdapter.setOnItemClick(this);
        mBinding.recyclerView.setAdapter(mAdapter);

        ResolRefreshQue.solveScoll(mBinding.recyclerView,mBinding.swipeToLoad);
        mBinding.swipeToLoad.setOnRefreshListener(this);
        mBinding.swipeToLoad.setOnLoadMoreListener(this);
        mBinding.swipeToLoad.post(() -> mBinding.swipeToLoad.setRefreshing(true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_comprehensive:
                //综合
                if (currentPosition != 0){
                    if (refresh()) return;
                    currentPosition = 0;
                    mPresenter.initText(this,currentPosition,mTextViews);
                    mBinding.upBlack.setColorFilter(Color.BLACK);
                    mBinding.downBlack.setColorFilter(Color.BLACK);
                    sort = 1;
                    mPresenter.goodsList(id,currentPosition,sort,"1",this.bindUntilEvent(ActivityEvent.DESTROY));
                }
                break;
            case R.id.title_salesVolume:
                //销量
                if (currentPosition != 1){
                    if (refresh()) return;
                    currentPosition = 1;
                    mPresenter.initText(this,currentPosition,mTextViews);
                    mBinding.upBlack.setColorFilter(Color.BLACK);
                    mBinding.downBlack.setColorFilter(Color.BLACK);
                    sort = 1;
                    mPresenter.goodsList(id,currentPosition,sort,"1",this.bindUntilEvent(ActivityEvent.DESTROY));
                }
                break;
            case R.id.title_new:
                //上新
                if (currentPosition != 2){
                    if (refresh()) return;
                    currentPosition = 2;
                    mPresenter.initText(this,currentPosition,mTextViews);
                    mBinding.upBlack.setColorFilter(Color.BLACK);
                    mBinding.downBlack.setColorFilter(Color.BLACK);
                    sort = 1;
                    mPresenter.goodsList(id,currentPosition,sort,"1",this.bindUntilEvent(ActivityEvent.DESTROY));
                }
                break;
            case R.id.title_price:
                //价格
                if (refresh()) return;
                currentPosition = 3;
                mPresenter.initText(this,currentPosition,mTextViews);
                if (sort == 0){
                    //降序
                    sort = 1;
                    mBinding.upBlack.setColorFilter(Color.BLACK);
                    mBinding.downBlack.setColorFilter(getResources().getColor(R.color.color_DC3D27));
                }else {
                    //升序
                    sort = 0;
                    mBinding.upBlack.setColorFilter(getResources().getColor(R.color.color_DC3D27));
                    mBinding.downBlack.setColorFilter(Color.BLACK);
                }
                mPresenter.goodsList(id,currentPosition,sort,"1",this.bindUntilEvent(ActivityEvent.DESTROY));
                break;
        }
    }

    //刷新
    @Override
    public void onRefresh() {
        page = 1;
        refersh = true;
        mPresenter.goodsList(id,currentPosition,sort,page+"",this.bindUntilEvent(ActivityEvent.DESTROY));
    }

    //加载
    @Override
    public void onLoadMore() {
        page++;
        refersh = false;
        mPresenter.goodsList(id,currentPosition,sort,page+"",this.bindUntilEvent(ActivityEvent.DESTROY));
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
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
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

    /**
     * 刷新
     */
    public Boolean refresh(){
        if (mDialog != null && mDialog.isShowing()){
            ToastUtil.show("数据正在请求，请稍等");
            return true;
        }
        refersh = true;
        mDialog = (new ProgressDialogView()).createLoadingDialog(this, "");
        mDialog.show();
        mBinding.recyclerView.scrollToPosition(0);
        return false;
    }

    public void initError() {
        mBinding.netClude.errorContainer.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }

    /**
     * item点击
     */
    @Override
    public void onItemLayoutClick(int position) {
        if (ClickUtil.isFastDoubleClick(800)) {
            return;
        }else{
            startActivity(new Intent(this,DetailActivity.class)
                    .putExtra("gid",mList.get(position).getGid())
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void cartpage(CartpageBus bus){
        if (bus != null){
            finish();
        }
    }

    @Override
    public void onSuccess(ListBean listBean) {
        if(refersh){
            dissRefresh();
        }else {
            dissLoading();
        }
        if (listBean.getGoodsList() != null && listBean.getGoodsList().size() != 0 && listBean.getPage().getPagecount() >= page){
            //有数据
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            mBinding.netClude.errorContainer.setVisibility(View.GONE);
            if(refersh){
                mList.clear();
            }
            mList.addAll(listBean.getGoodsList());
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
        ToastUtil.show(error);
    }
}
