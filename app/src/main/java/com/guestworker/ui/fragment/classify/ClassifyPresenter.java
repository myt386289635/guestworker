package com.guestworker.ui.fragment.classify;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guestworker.bean.ClassifyBean;
import com.guestworker.bean.SystemBean;
import com.guestworker.netwrok.Repository;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/12
 * @Describe
 */
public class ClassifyPresenter {

    private Repository mRepository;
    private ClassifyView mView;

    public void setView(ClassifyView view) {
        mView = view;
    }

    @Inject
    public ClassifyPresenter(Repository repository) {
        mRepository = repository;
    }

    public void setStatusBarHight(LinearLayout StatusBar , Context context){
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            ViewGroup.LayoutParams layoutParams = StatusBar.getLayoutParams();
            layoutParams.height = statusBarHeight;
        }
    }

    /**
     * 滑动到中间布局
     */
    public void scrollToMiddleH(RecyclerView recyclerView, LinearLayoutManager linearLayoutManager, View view, int position) {
        int vHeight = view.getHeight();
        Rect rect = new Rect();
        recyclerView.getGlobalVisibleRect(rect);
        int reHeight =rect.bottom - rect.top - vHeight;
        final int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int top = recyclerView.getChildAt(position - firstPosition).getTop();
        int half = reHeight / 2;
        recyclerView.smoothScrollBy( 0,top - half);
    }

    public void goodsTypes(String parentgid, LifecycleTransformer<ClassifyBean> transformer){
        mRepository.goodsTypes(parentgid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(classifyBean -> {
                    if (classifyBean.isSuccess()){
                        if (mView != null){
                            mView.onItemSuccess(classifyBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onItemFile(classifyBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onItemFile(throwable.getMessage());
                    }
                });
    }

    public void getDate(LifecycleTransformer<ClassifyBean> transformer){
        mRepository.goodsTypes("0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(classifyBean -> {
                    if (classifyBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(classifyBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile(classifyBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onFile(throwable.getMessage());
                    }
                });
    }

    public void getSystem(LifecycleTransformer<SystemBean> transformer){
        mRepository.systemVariables()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(systemBean -> {
                    if (systemBean.isSuccess()){
                        if (mView != null){
                            mView.onSysSuccess(systemBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onSysFile();
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onSysFile();
                    }
                });
    }
}
