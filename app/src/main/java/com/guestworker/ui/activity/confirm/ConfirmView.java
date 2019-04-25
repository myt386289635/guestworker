package com.guestworker.ui.activity.confirm;

import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;

/**
 * @author 莫小婷
 * @create 2019/4/23
 * @Describe
 */
public interface ConfirmView {
    void onSuccess(OrderSaveBean bean);
    void onFile(String error);

    void onPaySuccess(PayCodeBean bean);
    void onPayFile(String error);
}
