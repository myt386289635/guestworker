package com.guestworker.ui.fragment.classify;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.blankj.utilcode.util.SPUtils;
import com.guestworker.R;
import com.guestworker.adapter.ClassifyContentAdapter;
import com.guestworker.adapter.ClassifyTitleAdapter;
import com.guestworker.base.BaseFragment;
import com.guestworker.bean.ClassifyBean;
import com.guestworker.bean.ClassifyTitleBean;
import com.guestworker.bean.SystemBean;
import com.guestworker.databinding.FragmentClassifyBinding;
import com.guestworker.netwrok.RetrofitModule;
import com.guestworker.ui.activity.shoplist.ListActivity;
import com.guestworker.util.ClickUtil;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.dialog.ProgressDialogView;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/9
 * @Describe  分类
 */
public class ClassifyFragment extends BaseFragment implements OnRefreshListener, ClassifyTitleAdapter.OnItemClick, ClassifyContentAdapter.OnItemClick, View.OnClickListener, ClassifyView {

    @Inject
    ClassifyPresenter mPresenter;

    private FragmentClassifyBinding mBinding;

    private List<ClassifyTitleBean> mClassifyTitle;
    private ClassifyTitleAdapter mTitleAdapter;

    private List<ClassifyBean.ChildTypesBean> mClassifyContent;
    private List<String> mBannerList;
    private ClassifyContentAdapter mContentAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int currentPosition = 0;//记录上一个点击的位置
    private Dialog mDialog;

    public static ClassifyFragment getInstance() {
        ClassifyFragment fragment = new ClassifyFragment();
        return fragment;
    }

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_classify,container,false);
        mBinding.setListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void initView() {
        mBaseFragmentComponent.inject(this);
        mPresenter.setStatusBarHight(mBinding.statusBar,getContext());
        mPresenter.setView(this);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.classifyTitle.setLayoutManager(mLinearLayoutManager);
        mClassifyTitle = new ArrayList<>();
        mTitleAdapter = new ClassifyTitleAdapter(mClassifyTitle,getContext());
        mTitleAdapter.setOnItemClick(this);
        mBinding.classifyTitle.setAdapter(mTitleAdapter);

        mBinding.classifyContent.setLayoutManager(new GridLayoutManager(getContext(),3));
        mClassifyContent = new ArrayList<>();
        mBannerList = new ArrayList<>();
        mContentAdapter = new ClassifyContentAdapter(mClassifyContent,getContext());
        mContentAdapter.setBannerList(mBannerList);
        mContentAdapter.setOnItemClick(this);
        mBinding.classifyContent.setAdapter(mContentAdapter);

        mBinding.swipeToLoad.setOnRefreshListener(this);
        mBinding.swipeToLoad.post(() -> mBinding.swipeToLoad.setRefreshing(true));
    }

    @Override
    public void onRefresh() {
        mPresenter.getSystem(this.bindToLifecycle());
    }

    /**
     * 点击title 刷新内容
     * @param position
     */
    @Override
    public void onItemClick(TextView textView , int position) {
        //滑动到中间
        if(currentPosition == position ){
            return;
        }
        mPresenter.scrollToMiddleH(mBinding.classifyTitle,mLinearLayoutManager,textView,position);
        mClassifyTitle.get(currentPosition).setUp(false);
        mClassifyTitle.get(position).setUp(true);
        currentPosition = position;
        mTitleAdapter.notifyDataSetChanged();
        mContentAdapter.setName(mClassifyTitle.get(position).getTypesBean().getGtname());
        mDialog = (new ProgressDialogView()).createLoadingDialog(getContext(), "");
        mDialog.show();
        mPresenter.goodsTypes(mClassifyTitle.get(position).getTypesBean().getGtid(),this.bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }

    /**
     * 商品点击
     */
    @Override
    public void onItemLayoutClick(int position) {
        if (ClickUtil.isFastDoubleClick(800)) {
            return;
        }else{
            startActivity(new Intent(getContext(),ListActivity.class)
                    .putExtra("title",mClassifyContent.get(position).getGtname())
                    .putExtra("gtid",mClassifyContent.get(position).getGtid())
            );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_search:
                // TODO: 2019/4/15 搜索
                ToastUtil.show("1.0.0版本不开发");
                break;
        }
    }

    @Override
    public void onSuccess(ClassifyBean classifyBean) {
        if (mBinding.swipeToLoad != null && mBinding.swipeToLoad.isRefreshing()) {
            mBinding.swipeToLoad.setRefreshing(false);
        }
        mBinding.swipeToLoad.setRefreshEnabled(false);//禁止刷新
        for (int i = 0; i < classifyBean.getParentTypes().size(); i++) {
            ClassifyTitleBean bean;
            if(i == 0){
                bean = new ClassifyTitleBean(classifyBean.getParentTypes().get(i),true);
            }else {
                bean = new ClassifyTitleBean(classifyBean.getParentTypes().get(i),false);
            }
            mClassifyTitle.add(bean);
        }
        mTitleAdapter.notifyDataSetChanged();

        mBannerList.add("");
        mContentAdapter.setName(mClassifyTitle.get(0).getTypesBean().getGtname());
        mClassifyContent.addAll(classifyBean.getChildTypes());
        mContentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFile(String error) {
        if (mBinding.swipeToLoad != null && mBinding.swipeToLoad.isRefreshing()) {
            mBinding.swipeToLoad.setRefreshing(false);
        }
        mBinding.swipeToLoad.setRefreshEnabled(false);//禁止刷新
        ToastUtil.show(error);
    }

    @Override
    public void onItemSuccess(ClassifyBean classifyBean) {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        mBannerList.clear();
        mClassifyContent.clear();
        mBannerList.add("");
        mClassifyContent.addAll(classifyBean.getChildTypes());
        mContentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemFile(String error) {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        ToastUtil.show(error);
    }

    @Override
    public void onSysSuccess(SystemBean bean) {
        SPUtils.getInstance().put(CommonDate.IMG_URL,bean.getVariableMap().getImageserver_path());
        RetrofitModule.IMG_URL = bean.getVariableMap().getImageserver_path();
        mPresenter.getDate(this.bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }

    @Override
    public void onSysFile() {
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(CommonDate.IMG_URL,""))){
            RetrofitModule.IMG_URL = SPUtils.getInstance().getString(CommonDate.IMG_URL);
        }else {
            ToastUtil.show("网络请求错误");
        }
        mPresenter.getDate(this.bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }
}
