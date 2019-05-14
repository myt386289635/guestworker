package com.guestworker.ui.activity.user.areaMembers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.guestworker.R;
import com.guestworker.adapter.AreaUserAdapter;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.AreaUserBean;
import com.guestworker.bean.UserBean;
import com.guestworker.databinding.ActivityUserBinding;
import com.guestworker.ui.activity.user.address.CreateAddressActivity;
import com.guestworker.util.ResolRefreshQue;
import com.guestworker.util.ToastUtil;
import com.guestworker.view.dialog.DialogUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe  代客下单时选择用户
 */
public class AreaUserActivity extends BaseActivity implements View.OnClickListener, AreaUserView, OnRefreshListener, OnLoadMoreListener, AreaUserAdapter.OnItemClick, TextWatcher {

    @Inject
    AreaUserPresenter mPresenter;
    private ActivityUserBinding mBinding;
    private List<UserBean> mList;
    private AreaUserAdapter mAdapter;
    private int set = -1;//选择的位置
    private int page = 1;
    private Boolean refersh = true;
    private String searchVal = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_user);
        mBinding.setTag(getIntent().getBooleanExtra("isMineModel",false));
        mBinding.setListener(this);
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        initView();
    }

    private void initView() {
        mBinding.inClude.titleTv.setText(getIntent().getStringExtra("title"));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new AreaUserAdapter(mList,this);
        mAdapter.setOnItemClick(this);
        mBinding.recyclerView.setAdapter(mAdapter);

        ResolRefreshQue.solveScoll(mBinding.recyclerView,mBinding.swipeToLoad);
        mBinding.sreachEdit.addTextChangedListener(this);
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
            case R.id.user_save:
                ///保存
                if (set == -1){
                    ToastUtil.show("请选择一个会员");
                    return;
                }
                if (getIntent().getBooleanExtra("isHome",false)){
                    //刷新首页购物车
                    EventBus.getDefault().post(mList.get(set).getBean());
                }
                Intent intent = new Intent();
                intent.putExtra("date",mList.get(set).getBean());
                setResult(201,intent);
                finish();
                break;
            case R.id.sreach_sreach:
                //搜索会员
                if (TextUtils.isEmpty(mBinding.sreachEdit.getText().toString().trim())) {
                    ToastUtil.show("请输入搜索内容");
                    return;
                }
                searchVal = mBinding.sreachEdit.getText().toString().trim();
                if (!mBinding.swipeToLoad.isRefreshEnabled()) {
                    mBinding.swipeToLoad.setRefreshEnabled(true);
                    mBinding.swipeToLoad.setRefreshing(true);
                    mBinding.swipeToLoad.setRefreshEnabled(false);
                } else {
                    mBinding.swipeToLoad.setRefreshing(true);
                }
                break;
            case R.id.sreach_close:
                //清空搜索框
                mBinding.sreachEdit.setText("");
                break;
        }
    }

    @Override
    public void onSuccess(AreaUserBean areaUserBean) {
        if(refersh){
            dissRefresh();
        }else {
            dissLoading();
        }
        if (areaUserBean.getAreaMemberList() != null && areaUserBean.getAreaMemberList().size() != 0 && areaUserBean.getPage().getPagecount() >= page){
            //有数据
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            mBinding.netClude.errorContainer.setVisibility(View.GONE);
            if(refersh){
                mList.clear();
                set = -1;
            }
            for (int i = 0; i < areaUserBean.getAreaMemberList().size(); i++) {
                UserBean userBean = new UserBean(false,areaUserBean.getAreaMemberList().get(i));
                mList.add(userBean);
            }
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

    @Override
    public void onRefresh() {
        page = 1;
        refersh = true;
        mPresenter.getAreaMember(this,page + "" ,searchVal, this.bindUntilEvent(ActivityEvent.DESTROY));
    }

    @Override
    public void onLoadMore() {
        page++;
        refersh = false;
        mPresenter.getAreaMember(this,page + "",searchVal, this.bindUntilEvent(ActivityEvent.DESTROY));
    }

    @Override
    public void onItemClick(int position) {
        if (set != -1) {
            mList.get(set).setTag(false);
        }
        if (TextUtils.isEmpty(mList.get(position).getBean().getAddress())){
            //没有地址
            set = -1;
            mAdapter.notifyDataSetChanged();
            DialogUtil.LoginDialog(this, "该用户没有地址，是否添加", "确认", "取消", v1 ->{
                startActivityForResult(new Intent(AreaUserActivity.this, CreateAddressActivity.class)
                                .putExtra("userid",mList.get(position).getBean().getUserid())
                                .putExtra("username",mList.get(position).getBean().getUsername())
                                .putExtra("mobile",mList.get(position).getBean().getMobile())
                        ,333);
            });
            return;
        }
        if (mList.get(position).getTag()) {
            mList.get(position).setTag(false);
        } else {
            mList.get(position).setTag(true);
        }
        mAdapter.notifyDataSetChanged();
        set = position;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 333:
                if (!mBinding.swipeToLoad.isRefreshEnabled()) {
                    mBinding.swipeToLoad.setRefreshEnabled(true);
                    mBinding.swipeToLoad.setRefreshing(true);
                    mBinding.swipeToLoad.setRefreshEnabled(false);
                } else {
                    mBinding.swipeToLoad.setRefreshing(true);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(mBinding.sreachEdit.getText().toString().trim())) {
            mBinding.sreachClose.setVisibility(View.GONE);
            searchVal = "";
            if (!mBinding.swipeToLoad.isRefreshEnabled()) {
                mBinding.swipeToLoad.setRefreshEnabled(true);
                mBinding.swipeToLoad.setRefreshing(true);
                mBinding.swipeToLoad.setRefreshEnabled(false);
            } else {
                mBinding.swipeToLoad.setRefreshing(true);
            }
        } else {
            mBinding.sreachClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                showKeyboard(false);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
