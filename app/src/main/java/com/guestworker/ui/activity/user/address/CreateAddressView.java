package com.guestworker.ui.activity.user.address;

import com.guestworker.bean.AddressBean;

/**
 * @author 莫小婷
 * @create 2019/3/6
 * @Describe
 */
public interface CreateAddressView {
    void onSuccess(AddressBean bean);
    void onFile(String error);
}
