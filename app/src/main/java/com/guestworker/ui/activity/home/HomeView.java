package com.guestworker.ui.activity.home;

import com.guestworker.bean.IsLoginBean;
import com.guestworker.bean.SystemBean;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe
 */
public interface HomeView {

    void onisloginSuc(IsLoginBean isLoginBean);
    void onisloginFile();
}
