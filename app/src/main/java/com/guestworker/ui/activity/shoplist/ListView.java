package com.guestworker.ui.activity.shoplist;

import com.guestworker.bean.ListBean;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public interface ListView {

    void onSuccess(ListBean listBean);
    void onFile(String error);
}
