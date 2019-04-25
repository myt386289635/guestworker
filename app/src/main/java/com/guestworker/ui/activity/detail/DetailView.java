package com.guestworker.ui.activity.detail;

import com.guestworker.bean.DetailBean;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public interface DetailView {

    void onSuccess(DetailBean bean);
    void onFile(String error);
}
