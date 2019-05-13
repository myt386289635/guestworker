package com.guestworker.ui.activity.user.invitation;

import com.guestworker.bean.InvitationBean;
import com.guestworker.netwrok.Repository;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/5/13
 * @Describe
 */
public class InvitationPresenter {

    private Repository mRepository;
    private InvitationView mView;

    public void setView(InvitationView view) {
        mView = view;
    }

    @Inject
    public InvitationPresenter(Repository repository) {
        mRepository = repository;
    }

    public void salescode(LifecycleTransformer<InvitationBean> transformer){
        mRepository.salescode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(invitationBean -> {
                    if (invitationBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(invitationBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile();
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onFile();
                    }
                });
    }

}
