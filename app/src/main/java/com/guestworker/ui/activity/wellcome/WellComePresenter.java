package com.guestworker.ui.activity.wellcome;

import com.guestworker.bean.IsLoginBean;
import com.guestworker.netwrok.Repository;
import com.guestworker.ui.activity.home.HomeView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/28
 * @Describe
 */
public class WellComePresenter {

    private Repository mRepository;
    private HomeView mView;

    public void setView(HomeView view) {
        mView = view;
    }

    @Inject
    public WellComePresenter(Repository repository) {
        mRepository = repository;
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
