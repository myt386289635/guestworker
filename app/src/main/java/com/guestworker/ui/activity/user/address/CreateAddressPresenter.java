package com.guestworker.ui.activity.user.address;

import com.guestworker.bean.AddressBean;
import com.guestworker.netwrok.Repository;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/3/6
 * @Describe
 */
public class CreateAddressPresenter {

    private Repository mRepository;
    private CreateAddressView mView;

    public void setView(CreateAddressView view) {
        mView = view;
    }

    @Inject
    public CreateAddressPresenter(Repository repository) {
        mRepository = repository;
    }

    /**
     * 新增地址
     */
    public void addressAdd(String username, String mobile,String address,String areaCode, String userId,LifecycleTransformer<AddressBean> transformer){
        mRepository.addAddress(username,mobile,address,areaCode,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(addressBean -> {
                    if (addressBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(addressBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile(addressBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onFile(throwable.getMessage());
                    }
                });
    }

}
