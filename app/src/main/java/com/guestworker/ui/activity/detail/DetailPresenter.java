package com.guestworker.ui.activity.detail;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guestworker.R;
import com.guestworker.adapter.DetailBannerAdapter;
import com.guestworker.bean.DetailBean;
import com.guestworker.netwrok.Repository;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/16
 * @Describe
 */
public class DetailPresenter {

    private Repository mRepository;
    private DetailView mView;

    public void setView(DetailView view) {
        mView = view;
    }

    @Inject
    public DetailPresenter(Repository repository) {
        mRepository = repository;
    }

    public void setStatusBarHight(LinearLayout StatusBar, TextView textView, Context context){
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            ViewGroup.LayoutParams layoutParams = StatusBar.getLayoutParams();
            layoutParams.height = statusBarHeight;

            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams1.topMargin = statusBarHeight;
        }
    }

    public void initWebView(WebView mWebView){
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setJavaScriptEnabled(true);//启用支持javascript
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        mWebView.getSettings().setDomStorageEnabled(true);// 开启 DOM storage API 功能
    }

    public void initBanner(List<String> mBannerList , Context context ,
                           List<ImageView> point, LinearLayout mDetailPoint,
                           DetailBannerAdapter mBannerAdapter){
        point.clear();
        mDetailPoint.removeAllViews();
        for (int i = 0; i < mBannerList.size(); i++) {
            ImageView imageView = new ImageView(context);
            point.add(imageView);
            if (i == 0) {
                imageView.setImageResource(R.drawable.banner_down);
            } else {
                imageView.setImageResource(R.drawable.banner_up);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin =context.getResources().getDimensionPixelSize(R.dimen.x4);
            layoutParams.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.x4);
            mDetailPoint.addView(imageView, layoutParams);
        }
        mBannerAdapter.notifyDataSetChanged();
    }

    public void getDate(String gid , LifecycleTransformer<DetailBean> transformer){
        mRepository.goodsDetail(gid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(detailBean -> {
                    if (detailBean.getDefaultgoods() != null){
                        if (mView != null){
                            mView.onSuccess(detailBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile(detailBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onFile(throwable.getMessage());
                    }
                });
    }
}
