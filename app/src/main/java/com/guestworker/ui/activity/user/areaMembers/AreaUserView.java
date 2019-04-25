package com.guestworker.ui.activity.user.areaMembers;

import com.guestworker.bean.AreaUserBean;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public interface AreaUserView {

    void onSuccess(AreaUserBean areaUserBean);
    void onFile(String error);
}
