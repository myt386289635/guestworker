package com.guestworker.ui.activity.user;

import com.guestworker.bean.MyUserBean;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public interface UserView {

    void onSuccess(MyUserBean myUserBean);
    void onFile(String error);
}
