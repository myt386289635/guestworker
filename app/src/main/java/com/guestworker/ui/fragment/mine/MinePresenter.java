package com.guestworker.ui.fragment.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.guestworker.bean.IsLoginBean;
import com.guestworker.bean.UserInfoBean;
import com.guestworker.netwrok.Repository;
import com.guestworker.ui.activity.login.LoginActivity;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.dialog.DialogUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe
 */
public class MinePresenter {

    private Repository mRepository;
    private MineView mView;

    public void setView(MineView view) {
        mView = view;
    }

    @Inject
    public MinePresenter(Repository repository) {
        mRepository = repository;
    }

    public void setStatusBarHight(LinearLayout StatusBar , Context context){
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            ViewGroup.LayoutParams layoutParams = StatusBar.getLayoutParams();
            layoutParams.height = statusBarHeight;
        }
    }

    public void getUserInfo(LifecycleTransformer<UserInfoBean> transformer){
        mRepository.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(isLoginBean -> {
                    if (isLoginBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(isLoginBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile();
                        }
                    }
                }, throwable -> {
                    if (throwable instanceof HttpException){
                        HttpException exception = (HttpException)throwable;
                        int code = exception.response().code();
                        if (code == 401){
                           //登陆失效了
                            if (mView != null){
                                mView.onInvalid();
                            }
                        }else {
                            if (mView != null){
                                mView.onFile();
                            }
                        }
                    }else {
                        if (mView != null){
                            mView.onFile();
                        }
                    }
                });
    }
}
