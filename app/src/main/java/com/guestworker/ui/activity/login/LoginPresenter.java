package com.guestworker.ui.activity.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.guestworker.R;
import com.guestworker.bean.LoginBean;
import com.guestworker.netwrok.Repository;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe
 */
public class LoginPresenter {

    private Repository mRepository;
    private LoginView mView;

    public void setView(LoginView view) {
        mView = view;
    }

    @Inject
    public LoginPresenter(Repository repository) {
        mRepository = repository;
    }

    /**
     * 为了设置登陆按钮的背景颜色
     */
    public void initLoginButton(final EditText number, final EditText code, final Button loginButton) {
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(code.getText().toString())) {
                    if (TextUtils.isEmpty(number.getText().toString())){
                        loginButton.setEnabled(false);
                        loginButton.setBackgroundResource(R.drawable.bg_unlogin);
                    } else {
                        loginButton.setEnabled(true);
                        loginButton.setBackgroundResource(R.drawable.bg_logined);
                    }
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundResource(R.drawable.bg_unlogin);
                }
            }
        });

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(number.getText().toString())) {
                    if (TextUtils.isEmpty(code.getText().toString())){
                        loginButton.setEnabled(false);
                        loginButton.setBackgroundResource(R.drawable.bg_unlogin);
                    } else {
                        loginButton.setEnabled(true);
                        loginButton.setBackgroundResource(R.drawable.bg_logined);
                    }
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundResource(R.drawable.bg_unlogin);
                }
            }
        });
    }

    public void login(String mobile, String password , LifecycleTransformer<LoginBean> transformer){
        mRepository.login(mobile, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(loginBean -> {
                    if (loginBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(loginBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile(loginBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onFile(throwable.getMessage());
                    }
                });
    }
}
