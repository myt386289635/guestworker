package com.guestworker.base;

import android.app.Service;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.guestworker.MyApplication;
import com.guestworker.R;
import com.guestworker.util.InputMethodManagerLeak;
import com.guestworker.util.MainThreadPoster;
import com.guestworker.util.NotchUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.Unbinder;

public class BaseActivity extends RxAppCompatActivity {

    protected BaseActivityComponent mBaseActivityComponent;
    protected Unbinder mButterKnife;

    protected InputMethodManager mImm;
    protected String HANDLER_TOKEN = getClass().getName();
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivityComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(MyApplication.getInstance().getComponent())
                .build();

        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.color_white)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0f)
                .init();

        mImm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        if (Build.VERSION.SDK_INT >= 28) {
            //适配9.0刘海
            NotchUtil.notch(this);
        }

        // 添加Activity到堆栈
        MyApplication.getInstance().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        if (mImmersionBar != null)
            mImmersionBar.destroy(); //必须调用该方法，防止内存泄漏
        InputMethodManagerLeak.fixInputMethodManagerLeak(mImm,this);
        mImm = null;
        MainThreadPoster.clear(HANDLER_TOKEN);
        if (mButterKnife != null){
            mButterKnife.unbind();
        }
        super.onDestroy();
    }


    protected void showKeyboard(boolean isShow) {
        if (isShow) {
            if (getCurrentFocus() == null) {
                mImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                mImm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                mImm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }

        MainThreadPoster.postRunnableDelay(new Runnable() {
            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, HANDLER_TOKEN, 200);
    }

}
