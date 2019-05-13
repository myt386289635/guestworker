package com.guestworker.ui.fragment.mine;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.guestworker.R;
import com.guestworker.base.BaseFragment;
import com.guestworker.bean.UserInfoBean;
import com.guestworker.bean.eventbus.RefreshCartBus;
import com.guestworker.databinding.FragmentMineBinding;
import com.guestworker.ui.activity.login.LoginActivity;
import com.guestworker.ui.activity.user.UserActivity;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.cookie.MyCookieJar;
import com.guestworker.util.glide.GlideApp;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.dialog.DialogUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/9
 * @Describe
 */
public class MineFragment  extends BaseFragment implements View.OnClickListener, MineView {

    private FragmentMineBinding mBinding;
    @Inject
    MinePresenter mPresenter;

    public static MineFragment getInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_mine,container,false);
        mBinding.setListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void initView() {
        mBaseFragmentComponent.inject(this);
        mPresenter.setStatusBarHight(mBinding.statusBar,getContext());
        mPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SPUtils.getInstance(CommonDate.USER).getBoolean(CommonDate.LOGIN,false)){
            //登录过
            mPresenter.getUserInfo(this.bindUntilEvent(FragmentEvent.DESTROY_VIEW));
        }else {
            //未登录
            mBinding.mineName.setVisibility(View.INVISIBLE);
            mBinding.mineCustom.setVisibility(View.GONE);
            mBinding.mineCode.setVisibility(View.INVISIBLE);
            mBinding.mineLogin.setText("登录");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_login:
                if (mBinding.mineLogin.getText().toString().equals("登录")){
                    //登录
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }else {
                    //退出登录
                    DialogUtil.LoginDialog(getContext(), "您确定要退出登录吗？", "确定", "取消", v1 -> {
                        mBinding.mineName.setVisibility(View.INVISIBLE);
                        mBinding.mineCode.setVisibility(View.INVISIBLE);
                        mBinding.mineCustom.setVisibility(View.GONE);
                        mBinding.mineLogin.setText("登录");
                        ToastUtil.show("退出登录成功");
                        MyCookieJar.getInstance().removeAll();
                        SPUtils.getInstance(CommonDate.USER).clear();
                        EventBus.getDefault().post(new RefreshCartBus());//刷新购物车
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    });
                }
                break;
            case R.id.mine_custom:
                //我的客户
                startActivity(new Intent(getContext(),UserActivity.class)
                        .putExtra("title","我的客户")
                        .putExtra("isMineModel",true)
                );
                break;
        }
    }

    public String editPhone(String phone){
        if(!TextUtils.isEmpty(phone)){
            String start = phone.substring(0,3);
            String end = phone.substring(phone.length() - 4,phone.length());
            start = start + "****" + end;
            return start;
        }
        return phone;
    }

    /**
     * 获取用户信息成功
     * @param bean
     */
    @Override
    public void onSuccess(UserInfoBean bean) {

        SPUtils.getInstance(CommonDate.USER).put(CommonDate.NAME,bean.getSalesInfo().getUsername());
        SPUtils.getInstance(CommonDate.USER).put(CommonDate.userheadpath,bean.getSalesInfo().getUserheadpath());
        SPUtils.getInstance(CommonDate.USER).put(CommonDate.userid,bean.getSalesInfo().getSalesid());
        SPUtils.getInstance(CommonDate.USER).put(CommonDate.salescode,bean.getSalesInfo().getSalescode());

        mBinding.mineName.setVisibility(View.VISIBLE);
        mBinding.mineCode.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(bean.getSalesInfo().getUsername())){
            mBinding.mineName.setText(editPhone(bean.getSalesInfo().getMobile()));
        }else {
            mBinding.mineName.setText(bean.getSalesInfo().getUsername());
        }
        mBinding.mineCode.setText("邀请码：" + bean.getSalesInfo().getSalescode());
        GlideApp.loderCircleImage(getContext(),bean.getSalesInfo().getUserheadpath(),mBinding.mineImage,R.mipmap.default_img,0);
        mBinding.mineLogin.setText("退出账号");
        mBinding.mineCustom.setVisibility(View.VISIBLE);
    }

    /**
     * 获取用户信息失败
     */
    @Override
    public void onFile() {
        if (SPUtils.getInstance(CommonDate.USER).getBoolean(CommonDate.LOGIN,false)){
            mBinding.mineName.setVisibility(View.VISIBLE);
            mBinding.mineCode.setVisibility(View.VISIBLE);
            mBinding.mineCustom.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.NAME,""))){
                mBinding.mineName.setText(editPhone(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE)));
            }else {
                mBinding.mineName.setText(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.NAME));
            }
            mBinding.mineCode.setText("邀请码：" + SPUtils.getInstance(CommonDate.USER).getString(CommonDate.salescode));
            GlideApp.loderCircleImage(getContext(),SPUtils.getInstance(CommonDate.USER).getString(CommonDate.userheadpath,""),mBinding.mineImage,R.mipmap.default_img,0);
            mBinding.mineLogin.setText("退出账号");
        }else {
            mBinding.mineName.setVisibility(View.INVISIBLE);
            mBinding.mineCode.setVisibility(View.INVISIBLE);
            mBinding.mineCustom.setVisibility(View.GONE);
            mBinding.mineLogin.setText("登录");
            SPUtils.getInstance(CommonDate.USER).clear();
        }
    }

    /**
     * 登陆失效
     */
    @Override
    public void onInvalid() {
        if (SPUtils.getInstance(CommonDate.USER).getBoolean(CommonDate.LOGIN, false)) {
            //cookie失效了
            DialogUtil.SingleDialog(getContext(), "登录失效,请重新登陆", "好的", v -> startActivity(new Intent(getContext(), LoginActivity.class)));
        }
        mBinding.mineName.setVisibility(View.INVISIBLE);
        mBinding.mineCode.setVisibility(View.INVISIBLE);
        mBinding.mineCustom.setVisibility(View.GONE);
        mBinding.mineLogin.setText("登录");
        MyCookieJar.getInstance().removeAll();
        SPUtils.getInstance(CommonDate.USER).clear();
        EventBus.getDefault().post(new RefreshCartBus());//刷新购物车
    }
}
