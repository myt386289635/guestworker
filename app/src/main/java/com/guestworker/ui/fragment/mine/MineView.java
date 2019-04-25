package com.guestworker.ui.fragment.mine;

import com.guestworker.bean.UserInfoBean;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public interface MineView {

    void onSuccess(UserInfoBean bean);
    void onFile();
    void onInvalid();//失效
}
