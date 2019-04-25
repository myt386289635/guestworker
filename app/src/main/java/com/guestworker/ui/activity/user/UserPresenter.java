package com.guestworker.ui.activity.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.guestworker.bean.MyUserBean;
import com.guestworker.netwrok.Repository;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.cookie.HttpResponseFunc;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class UserPresenter {

    private Repository mRepository;
    private UserView mView;

    public void setView(UserView view) {
        mView = view;
    }

    @Inject
    public UserPresenter(Repository repository) {
        mRepository = repository;
    }

    /**
     * 获取我的客户列表
     */
    public void getMyMember(Context context, String pageon, LifecycleTransformer<MyUserBean> transformer){
        mRepository.myMembers(pageon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(myUserBean -> {
                    if (myUserBean.isSuccess()) {
                        if (mView != null) {
                            mView.onSuccess(myUserBean);
                        }
                    } else {
                        if (mView != null) {
                            mView.onFile("网络获取失败");
                            ToastUtil.show("网络获取失败");
                        }
                    }
                }, throwable -> {
                    if (!HttpResponseFunc.onError(context,throwable)){
                        ToastUtil.show("网络获取失败");
                    }
                    if (mView != null) {
                        mView.onFile("网络获取失败");
                    }
                });
    }
}
