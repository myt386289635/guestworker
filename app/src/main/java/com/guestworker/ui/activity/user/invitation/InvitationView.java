package com.guestworker.ui.activity.user.invitation;

import com.guestworker.bean.InvitationBean;

/**
 * @author 莫小婷
 * @create 2019/5/13
 * @Describe
 */
public interface InvitationView {

    void onSuccess(InvitationBean bean);
    void onFile();
}
