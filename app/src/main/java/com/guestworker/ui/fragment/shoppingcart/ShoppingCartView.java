package com.guestworker.ui.fragment.shoppingcart;

import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;
import com.guestworker.bean.PayResultBean;

/**
 * @author 莫小婷
 * @create 2019/4/23
 * @Describe
 */
public interface ShoppingCartView {

    void onSuccess(OrderSaveBean bean,String type);
    void onFile(String error);

    void onPaySuccess(PayCodeBean bean,String tradeType);
    void onPayFile(String error);

    void onPayResultSuc(PayResultBean bean);
}
