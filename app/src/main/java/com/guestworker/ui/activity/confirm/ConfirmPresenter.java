package com.guestworker.ui.activity.confirm;

import com.guestworker.bean.OrderBean;
import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;
import com.guestworker.bean.PayResultBean;
import com.guestworker.netwrok.Repository;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/23
 * @Describe
 */
public class ConfirmPresenter {

    private Repository mRepository;
    private ConfirmView mView;

    public void setView(ConfirmView view) {
        mView = view;
    }

    @Inject
    public ConfirmPresenter(Repository repository) {
        mRepository = repository;
    }

    /**
     * 生成订单
     */
    public void orderSave(OrderBean bean, String tradeType,LifecycleTransformer<OrderSaveBean> transformer){
        mRepository.orderSave(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(orderSaveBean -> {
                    if (orderSaveBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(orderSaveBean,tradeType);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile(orderSaveBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onFile(throwable.getMessage());
                    }
                });
    }

    /**
     * 生成二维码
     */
    public void payCode(String tradeNo, String tradeType,LifecycleTransformer<PayCodeBean> transformer){
        mRepository.payCode(tradeNo,tradeType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(payCodeBean -> {
                    if (payCodeBean.isSuccess()){
                        if (mView != null){
                            mView.onPaySuccess(payCodeBean,tradeType);
                        }
                    }else {
                        if (mView != null){
                            mView.onPayFile("支付失败：" + payCodeBean.getErr_code());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onPayFile(throwable.getMessage());
                    }
                });
    }

    /**
     * 支付回调轮训
     */
    public void payResult(String orderID , String userID, LifecycleTransformer<PayResultBean> transformer){
        mRepository.payResult(orderID, userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(payResultBean -> {
                    if (payResultBean.isSuccess()){
                        if (mView != null){
                            mView.onPayResultSuc(payResultBean);
                        }
                    }
                }, throwable -> {

                });
    }

}
