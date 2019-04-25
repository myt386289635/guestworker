package com.guestworker.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guestworker.MyApplication;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author 莫小婷
 * @create 2018/8/17
 * @Describe 给databinding的父类
 */
public abstract class BaseFragment extends RxFragment {

    protected BaseFragmentComponent mBaseFragmentComponent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseFragmentComponent = DaggerBaseFragmentComponent.builder()
                .applicationComponent(MyApplication.getInstance().getComponent())
                .build();
        return initLayout(inflater, container);
    }


    public abstract View initLayout(LayoutInflater inflater, ViewGroup container);

    public abstract void initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
