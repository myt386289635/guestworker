package com.guestworker.util.cookie;

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.SPUtils;
import com.guestworker.ui.activity.login.LoginActivity;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.dialog.DialogUtil;

import retrofit2.HttpException;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe  统一处理code码（感觉还是不太好，应该用一个统一的方式就不用每个地方都加这一句话了）
 */
public class HttpResponseFunc {


    public static Boolean onError(Context context, Throwable throwable){
        if (throwable instanceof HttpException){
            HttpException exception = (HttpException)throwable;
            int code = exception.response().code();
            if (code == 401){
                DialogUtil.SingleDialog(context, "登录失效,请重新登录", "好的", v -> {
                    context.startActivity(new Intent(context,LoginActivity.class));
                    SPUtils.getInstance(CommonDate.USER).clear();
                });
                return true;
            }
        }
        return false;
    }

}
