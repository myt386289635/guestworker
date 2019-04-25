package com.guestworker.ui.activity.login;

import com.guestworker.bean.LoginBean;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe
 */
public interface LoginView {

    void onSuccess(LoginBean bean);
    void onFile(String error);
}
