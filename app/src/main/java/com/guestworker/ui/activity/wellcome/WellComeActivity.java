package com.guestworker.ui.activity.wellcome;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.blankj.utilcode.util.SPUtils;
import com.guestworker.R;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.IsLoginBean;
import com.guestworker.ui.activity.home.HomeActivity;
import com.guestworker.ui.activity.home.HomeView;
import com.guestworker.ui.activity.login.LoginActivity;
import com.guestworker.util.cookie.MyCookieJar;
import com.guestworker.util.permission.HasPermissionsUtil;
import com.guestworker.util.sp.CommonDate;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yanzhenjie.permission.Permission;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2018/8/1
 * @Describe 欢迎页
 */
public class WellComeActivity extends BaseActivity implements HomeView {

    @Inject
    WellComePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        mImmersionBar.reset()
                .transparentStatusBar()
                .statusBarDarkFont(true, 0f)
                .init();
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        permission();
    }


    CountDownTimer timer = new CountDownTimer(2 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (SPUtils.getInstance(CommonDate.USER).getBoolean(CommonDate.LOGIN,false)){
                startActivity(new Intent(WellComeActivity.this, HomeActivity.class));
                finish();
            }else {
                startActivity(new Intent(WellComeActivity.this, LoginActivity.class)
                        .putExtra("isWellCome",true)
                );
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void permission(){
        HasPermissionsUtil.permission(this, new HasPermissionsUtil(){
            @Override
            public void hasPermissionsSuccess() {
                super.hasPermissionsSuccess();
                mPresenter.isLogin(WellComeActivity.this.bindUntilEvent(ActivityEvent.DESTROY));
            }

            @Override
            public void hasPermissionsFaile() {
                super.hasPermissionsFaile();
                mPresenter.isLogin(WellComeActivity.this.bindUntilEvent(ActivityEvent.DESTROY));
            }

            @Override
            public void rePermissionsFaile() {
                super.rePermissionsFaile();
                mPresenter.isLogin(WellComeActivity.this.bindUntilEvent(ActivityEvent.DESTROY));
            }

            @Override
            public void settingPermissions() {
                super.settingPermissions();
                mPresenter.isLogin(WellComeActivity.this.bindUntilEvent(ActivityEvent.DESTROY));
            }

        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    @Override
    public void onisloginSuc(IsLoginBean isLoginBean) {
        if (isLoginBean.getIsLogin() == 0){
            //未登录 或者cookie失效
            MyCookieJar.getInstance().removeAll();
            SPUtils.getInstance(CommonDate.USER).clear();
        }
        if (timer != null) {
            timer.start();
        }
    }

    @Override
    public void onisloginFile() {
        MyCookieJar.getInstance().removeAll();
        SPUtils.getInstance(CommonDate.USER).clear();
        if (timer != null) {
            timer.start();
        }
    }
}
