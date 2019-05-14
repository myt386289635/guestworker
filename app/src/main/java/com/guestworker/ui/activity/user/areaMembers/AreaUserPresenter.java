package com.guestworker.ui.activity.user.areaMembers;

import android.content.Context;
import android.text.TextUtils;

import com.guestworker.bean.AreaUserBean;
import com.guestworker.bean.MyUserBean;
import com.guestworker.netwrok.Repository;
import com.guestworker.netwrok.RetrofitModule;
import com.guestworker.ui.activity.user.UserView;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.cookie.HttpResponseFunc;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class AreaUserPresenter {

    private Repository mRepository;
    private AreaUserView mView;

    public void setView(AreaUserView view) {
        mView = view;
    }

    @Inject
    public AreaUserPresenter(Repository repository) {
        mRepository = repository;
    }

    /**
     * 选择用户列表
     */
    public void getAreaMember(Context context,String pageon,String searchVal, LifecycleTransformer<AreaUserBean> transformer){
        Map<String,String> map = new HashMap<>();
        map.put("pageon",pageon);
        map.put("pageSize", RetrofitModule.pageSize);
        if (!TextUtils.isEmpty(searchVal)){
            map.put("searchVal",searchVal);
        }
        mRepository.areaMembers(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(myUserBean -> {
                    if (myUserBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(myUserBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile("网络获取失败");
                            ToastUtil.show("网络获取失败");
                        }
                    }
                }, throwable -> {
                    if (!HttpResponseFunc.onError(context,throwable)){
                        ToastUtil.show("网络获取失败");
                    }
                    if (mView != null){
                        mView.onFile("网络获取失败");
                    }
                });
    }

}
