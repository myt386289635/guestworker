package com.guestworker.ui.activity.detail;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.guestworker.R;
import com.guestworker.adapter.DetailBannerAdapter;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.CartBean;
import com.guestworker.bean.DetailBean;
import com.guestworker.bean.eventbus.AddCartBus;
import com.guestworker.bean.eventbus.CartpageBus;
import com.guestworker.databinding.ActivityDetailBinding;
import com.guestworker.netwrok.RetrofitModule;
import com.guestworker.ui.activity.confirm.ConfirmActivity;
import com.guestworker.util.AnimatorUtils;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.WeakRefHandler;
import com.guestworker.util.cookie.GsonUtils;
import com.guestworker.util.share.ShareUtils;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.dialog.ProgressDialogView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/15
 * @Describe 商品详情页面
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener, DetailView {

    @Inject
    DetailPresenter mPresenter;
    private ActivityDetailBinding mBinding;
    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 100){
                if (mBinding.viewPager != null) {
                    mBinding.viewPager.setCurrentItem(mBinding.viewPager.getCurrentItem() + 1);
                }
            }else if(msg.what == 1){
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBinding.detailEvaluation.getLayoutParams();
                layoutParams.height = (int) (msg.arg1 * getResources().getDisplayMetrics().density);
                mBinding.detailEvaluation.setLayoutParams(layoutParams);
            }
            return true;
        }
    };
    private Handler mHandler = new WeakRefHandler(mCallback, Looper.getMainLooper());

    //banner
    private DetailBannerAdapter mBannerAdapter;
    private List<String> mBannerList;
    private List<ImageView> point;
    private Boolean stopThread = true;
    private Dialog mDialog;
    private DetailBean mDetailBean;//传递给确认订单页
    private Map<String,CartBean> mCartDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        mBinding.setListener(this);
        initView();
    }

    private void initView() {
        mImmersionBar.reset()
                .transparentStatusBar()
                .statusBarDarkFont(true, 0f)
                .init();
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        mPresenter.setStatusBarHight(mBinding.statusBar,mBinding.titleText,this);
        mBinding.scrollView.setOnScrollListener(top -> {
            //控制头布局显示与消失情况
            float a = top;
            float b = a / 1000;
            float max = (float) Math.min(1, b * 2);
            mBinding.titleContainer.setAlpha(max);
            //修改返回键和分享键的东西
            if (max >= 1){
                mBinding.titleBack.setBackgroundResource(0);
                mBinding.titleShare.setBackgroundResource(0);
                mBinding.titleBack.setColorFilter(Color.BLACK);
                mBinding.titleShare.setColorFilter(Color.BLACK);
            }else {
                mBinding.titleBack.setBackgroundResource(R.drawable.bg_back);
                mBinding.titleShare.setBackgroundResource(R.drawable.bg_back);
                mBinding.titleBack.setColorFilter(Color.WHITE);
                mBinding.titleShare.setColorFilter(Color.WHITE);
            }
        });
        mBinding.bottomService.setVisibility(View.VISIBLE);
        load();//初始化客工购物车

        //商品介绍
        mPresenter.initWebView(mBinding.detailEvaluation);
        mBinding.detailEvaluation.addJavascriptInterface(new WebViewJavaScriptFunction(), "android");
        mBinding.detailEvaluation.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.detailEvaluation.loadUrl("javascript:window.android.getBodyHeight(document.body.scrollHeight)");
            }
        });

        //轮播图
        mBannerAdapter = new DetailBannerAdapter(this);
        mBannerList = new ArrayList<>();
        point = new ArrayList<>();
        mBannerAdapter.setPoint(point);
        mBannerAdapter.setList(mBannerList);
        mBannerAdapter.setViewPager(mBinding.viewPager);
        mBinding.viewPager.setAdapter(mBannerAdapter);

        mDialog = (new ProgressDialogView()).createLoadingDialog(this, "");
        mDialog.show();
        mPresenter.getDate(getIntent().getStringExtra("gid"),this.bindToLifecycle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_backContainer:
                finish();
                break;
            case R.id.title_shareContainer:
                //分享
                if (mDetailBean == null) {
                    ToastUtil.show("数据获取错误，请重新打开页面");
                    return;
                }
                new ShareUtils(this, SHARE_MEDIA.WEIXIN)
                        .shareWeb(this, RetrofitModule.share_url + "/h5/goodsShare.html?gid=" + mDetailBean.getDefaultgoods().getGid() + "&guideid=" + SPUtils.getInstance(CommonDate.USER).getString(CommonDate.userid,""), mDetailBean.getDefaultgoods().getGname(), mDetailBean.getDefaultgoods().getOverview(), RetrofitModule.IMG_URL + mDetailBean.getDefaultgoods().getThumbnail(), R.mipmap.logo);
                break;
            case R.id.bottom_car:
                //加入购物车
                if (!SPUtils.getInstance(CommonDate.USER).getBoolean(CommonDate.LOGIN)){
                    ToastUtil.show("请先登陆");
                    return;
                }
                if (mDetailBean == null) {
                    ToastUtil.show("数据获取错误，请重新打开页面");
                    return;
                }
                if (mDetailBean.getDefaultgoods().getIsputon() == 0){
                    ToastUtil.show("此商品已下架");
                    return;
                }
                if (mDetailBean.getDefaultgoods().getStock() == 0 && mDetailBean.getDefaultgoods().getIsnegativestock() == 0){
                    ToastUtil.show("此商品库存不足无法购买");
                    return;
                }
                AnimatorUtils.doCartAnimator(this, mBinding.detailTag, mBinding.bottomCartImg, mBinding.container, animator -> {
                    addCart();
                    ToastUtil.show("加入购物车成功");
                    AnimatorUtils.viewRotate(mBinding.bottomCartImg);
                });
                break;
            case R.id.bottom_buy:
                //立即购买
                if (!SPUtils.getInstance(CommonDate.USER).getBoolean(CommonDate.LOGIN)){
                    ToastUtil.show("请先登陆");
                    return;
                }
                if (mDetailBean == null){
                    ToastUtil.show("数据获取错误，请重新打开页面");
                    return;
                }
                if (mDetailBean.getDefaultgoods().getIsputon() == 0){
                    ToastUtil.show("此商品已下架");
                    return;
                }
                if (mDetailBean.getDefaultgoods().getStock() == 0 && mDetailBean.getDefaultgoods().getIsnegativestock() == 0){
                    ToastUtil.show("此商品库存不足无法购买");
                    return;
                }
                startActivity(new Intent(this, ConfirmActivity.class)
                        .putExtra("date",mDetailBean)
                );
                break;
            case R.id.bottom_cart:
                //点击购物车
                EventBus.getDefault().post(new CartpageBus());
                finish();
                break;
            case R.id.bottom_service:
                //客服
                break;
        }
    }

    @Override
    public void onSuccess(DetailBean bean) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDetailBean = bean;
        mBinding.setDate(bean.getDefaultgoods());
        for (int i = 0; i < bean.getDefaultgoods().getGoodsPhotos().size(); i++) {
            mBannerList.add(RetrofitModule.IMG_URL + bean.getDefaultgoods().getGoodsPhotos().get(i).getPhotopath());
        }
        mPresenter.initBanner(mBannerList, this, point, mBinding.detailPoint, mBannerAdapter);
        new Thread(new MyRunble()).start();
    }

    @Override
    public void onFile(String error) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mBinding.netClude.errorContainer.setVisibility(View.VISIBLE);
        ToastUtil.show(error);
    }

    /******************轮播图需要*********************/
    public class MyRunble implements Runnable {

        @Override
        public void run() {
            while (stopThread) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(100);
            }
        }
    }

    /*****************webView需要*********************/
    public class WebViewJavaScriptFunction {
        /**
         * 高度
         */
        @JavascriptInterface
        public void getBodyHeight(String number) {
            int webViewHeight = Integer.parseInt(number.split("[.]")[0]);
//            Log.e("lgqqqqq======", "webViewHeight" + webViewHeight);
            Message msg = new Message();
            msg.what = 1;
            msg.arg1 = webViewHeight;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出该页面时刷新下购物车，避免点击加入购物车速度太快，刷新不了购物车列表
        EventBus.getDefault().post(new AddCartBus());
        stopThread = false;
        mHandler.removeCallbacksAndMessages(null);
        //防止webView内存溺出
        if (mBinding.detailEvaluation != null) {
            ViewParent parent = mBinding.detailEvaluation.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mBinding.detailEvaluation);
            }
            mBinding.detailEvaluation.stopLoading();
            mBinding.detailEvaluation.getSettings().setJavaScriptEnabled(false);
            mBinding.detailEvaluation.clearHistory();
            mBinding.detailEvaluation.clearView();
            mBinding.detailEvaluation.removeAllViews();
            mBinding.detailEvaluation.destroy();
        }
        UMShareAPI.get(this).release();//分享防止内存泄露
    }

    //初始化客工购物车需要做的
    private void load() {
        String cartDate = SPUtils.getInstance(CommonDate.CART).getString(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE, ""), "");
        mCartDate = GsonUtils.fromJson(cartDate, new TypeToken<Map<String, CartBean>>(){});
        if (mCartDate == null) { //说明手机里没有该客工购物车的值
            mCartDate = new HashMap<>();
        }
    }

    /**
     * 加入购物车
     */
    private void addCart(){
        CartBean cartBean = mCartDate.get(mDetailBean.getDefaultgoods().getGid());
        if (cartBean == null){
            //从来没有存储过该商品
            cartBean = new CartBean(1, mDetailBean);
        }else {
            //存储过该商品
            cartBean.setBean(mDetailBean);//更新商品
            cartBean.setNum(cartBean.getNum() + 1);//数量加一
        }
        mCartDate.put(mDetailBean.getDefaultgoods().getGid(),cartBean);
        //存储到本地
        String jsonStr = GsonUtils.toJson(mCartDate, new TypeToken<Map<String, CartBean>>(){});
        SPUtils.getInstance(CommonDate.CART).put(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE, ""),jsonStr);
    }
}
