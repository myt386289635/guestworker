package com.guestworker.ui.fragment.classify;

import com.guestworker.bean.ClassifyBean;
import com.guestworker.bean.SystemBean;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe
 */
public interface ClassifyView {

    void onSuccess(ClassifyBean classifyBean);
    void onFile(String error);

    void onItemSuccess(ClassifyBean classifyBean);
    void onItemFile(String error);

    void onSysSuccess(SystemBean bean);
    void onSysFile();
}
