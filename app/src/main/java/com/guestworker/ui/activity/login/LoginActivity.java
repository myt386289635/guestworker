package com.guestworker.ui.activity.login;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.guestworker.MyApplication;
import com.guestworker.R;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.LoginBean;
import com.guestworker.bean.eventbus.RefreshCartBus;
import com.guestworker.databinding.ActivityLoginBinding;
import com.guestworker.ui.activity.home.HomeActivity;
import com.guestworker.ui.activity.wellcome.WellComeActivity;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.dialog.ProgressDialogView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView {

    @Inject
    LoginPresenter mPresenter;

    private ActivityLoginBinding mBinding;
    private Dialog mDialog;
    private Boolean isWellCome = false;
    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        mBinding.setListener(this);
        initView();
    }

    private void initView() {
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        isWellCome = getIntent().getBooleanExtra("isWellCome",false);

        mBinding.loginLogin.setEnabled(false);
        mPresenter.initLoginButton(mBinding.loginNumber,mBinding.loginCode,mBinding.loginLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login:
                //登录
                mDialog = (new ProgressDialogView()).createLoadingDialog(this, "");
                mDialog.show();
                String code =  EncryptUtils.encryptMD5ToString(mBinding.loginCode.getText().toString().trim()).toLowerCase();
                mPresenter.login(mBinding.loginNumber.getText().toString(),code,this.bindToLifecycle());
                break;
            case R.id.title_back:
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    ToastUtil.show("再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    MyApplication.getInstance().exit();
                }
                break;
        }
    }

    @Override
    public void onSuccess(LoginBean bean) {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        SPUtils.getInstance(CommonDate.USER).put(CommonDate.LOGIN,true);
        SPUtils.getInstance(CommonDate.USER).put(CommonDate.PHONE,mBinding.loginNumber.getText().toString());
        EventBus.getDefault().post(new RefreshCartBus());//刷新购物车
        setResult(201);
        if (isWellCome){
            startActivity(new Intent(this, HomeActivity.class));
        }
        finish();
        ToastUtil.show("登录成功");
    }

    @Override
    public void onFile(String error) {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        ToastUtil.show(error);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.show("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            MyApplication.getInstance().exit();
        }
    }
}
