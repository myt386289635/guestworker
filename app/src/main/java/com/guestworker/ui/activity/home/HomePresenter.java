package com.guestworker.ui.activity.home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.guestworker.R;
import com.guestworker.bean.IsLoginBean;
import com.guestworker.bean.SystemBean;
import com.guestworker.netwrok.Repository;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/9
 * @Describe
 */
public class HomePresenter {

    private Repository mRepository;
    private HomeView mView;

    public void setView(HomeView view) {
        mView = view;
    }

    @Inject
    public HomePresenter(Repository repository) {
        mRepository = repository;
    }

    public void initTabLayout(Context context , TabLayout tabLayout){
        View view1 = LayoutInflater.from(context).inflate(R.layout.tablayout_item1,null);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.tablayout_item2,null);
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = LayoutInflater.from(context).inflate(R.layout.tablayout_item3,null);
        tabLayout.getTabAt(2).setCustomView(view3);

        //水波纹颜色
        tabLayout.setTabRippleColor(ColorStateList.valueOf(context.getResources().getColor(R.color.transparent)));
    }

    public void isLogin(LifecycleTransformer<IsLoginBean> transformer){
        mRepository.isLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(isLoginBean -> {
                    if (isLoginBean.isSuccess()){
                        if (mView != null){
                            mView.onisloginSuc(isLoginBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onisloginFile();
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onisloginFile();
                    }
                });
    }

}
