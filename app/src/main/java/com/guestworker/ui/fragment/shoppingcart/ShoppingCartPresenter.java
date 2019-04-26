package com.guestworker.ui.fragment.shoppingcart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guestworker.R;
import com.guestworker.bean.OrderBean;
import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;
import com.guestworker.bean.PayResultBean;
import com.guestworker.bean.ShoppingCartBean;
import com.guestworker.databinding.FragmentCartBinding;
import com.guestworker.netwrok.Repository;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/20
 * @Describe
 */
public class ShoppingCartPresenter {

    private Repository mRepository;
    private ShoppingCartView mView;

    public void setView(ShoppingCartView view) {
        mView = view;
    }

    @Inject
    public ShoppingCartPresenter(Repository repository) {
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

    public void initCartBuy(FragmentCartBinding mBinding){
        if (mBinding.cartBuy.getText().toString().equals("前往支付")){
            //点击编辑
            mBinding.cartEdit.setText("完成");
            mBinding.cartBuy.setText("删除");
            mBinding.cartPay.setVisibility(View.GONE);
            mBinding.cartPaytag.setVisibility(View.GONE);
            mBinding.confirmMoney.setVisibility(View.GONE);
        }else {
            //点击完成
            mBinding.cartEdit.setText("编辑");
            mBinding.cartBuy.setText("前往支付");
            mBinding.cartPay.setVisibility(View.VISIBLE);
            mBinding.cartPaytag.setVisibility(View.VISIBLE);
            mBinding.confirmMoney.setVisibility(View.VISIBLE);
        }
    }

    public void initError(FragmentCartBinding mBinding){
        mBinding.netClude.errorContainer.setVisibility(View.VISIBLE);
        mBinding.netClude.errorImage.setImageResource(R.mipmap.qs_commodity);
        mBinding.netClude.errorTitle.setText("暂无商品");
        mBinding.netClude.errorContent.setVisibility(View.GONE);
        mBinding.cartBottom.setVisibility(View.GONE);
        mBinding.cartEdit.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }

    /**
     * 恢复原来数据
     */
    public void initDate(FragmentCartBinding mBinding){
        if (!mBinding.cartBuy.getText().toString().equals("前往支付")){
            mBinding.cartEdit.setText("编辑");
            mBinding.cartBuy.setText("前往支付");
            mBinding.cartPay.setVisibility(View.VISIBLE);
            mBinding.cartPaytag.setVisibility(View.VISIBLE);
            mBinding.confirmMoney.setVisibility(View.VISIBLE);
        }
        mBinding.cartSelect.setImageResource(R.mipmap.cart_nor);
        mBinding.confirmMoney.setText("¥0");
    }

    /**
     * 计算总价格
     */
    public void getTotlePrice(List<ShoppingCartBean> mList, FragmentCartBinding mBinding, int position, boolean isSelect){
        Double totle = 0.0;
        if (position != -1){
            //被点击选择的
            totle = new BigDecimal(mBinding.confirmMoney.getText().toString().replace("¥","")).doubleValue();
            if (isSelect){
                //添加选择
                totle = totle + (mList.get(position).getCartBean().getNum() * mList.get(position).getCartBean().getBean().getDefaultgoods().getPrice());
            }else {
                totle = totle - (mList.get(position).getCartBean().getNum() * mList.get(position).getCartBean().getBean().getDefaultgoods().getPrice());
            }
        }else {
            //选择全选按钮
            for (int i = 0; i < mList.size(); i++) {
                totle = totle + (mList.get(i).getCartBean().getNum() * mList.get(i).getCartBean().getBean().getDefaultgoods().getPrice());
            }
        }
        totle = new BigDecimal(totle + "").setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        if(totle % 1 == 0){// 是这个整数，小数点后面是0
            int totle1 = totle.intValue();
            mBinding.confirmMoney.setText("¥" + totle1);
        }else {
            mBinding.confirmMoney.setText("¥" + totle);
        }
    }

    /**
     * 选中时加减需要更新价格
     */
    public void getTotle(List<ShoppingCartBean> mList, FragmentCartBinding mBinding,int position,boolean isIncrease){
        Double totle = 0.0;
        totle = new BigDecimal(mBinding.confirmMoney.getText().toString().replace("¥","")).doubleValue();
        if (isIncrease){
            //添加
            totle = totle + mList.get(position).getCartBean().getBean().getDefaultgoods().getPrice();
        }else {
            //删除
            totle = totle - mList.get(position).getCartBean().getBean().getDefaultgoods().getPrice();
        }
        totle = new BigDecimal(totle + "").setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        if(totle % 1 == 0){// 是这个整数，小数点后面是0
            int totle1 = totle.intValue();
            mBinding.confirmMoney.setText("¥" + totle1);
        }else {
            mBinding.confirmMoney.setText("¥" + totle);
        }
    }

    /**
     * 生成订单
     */
    public void orderSave(OrderBean bean, LifecycleTransformer<OrderSaveBean> transformer){
        mRepository.orderSave(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(orderSaveBean -> {
                    if (orderSaveBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(orderSaveBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile(orderSaveBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onFile(throwable.getMessage());
                    }
                });
    }

    /**
     * 生成二维码
     */
    public void payCode(String tradeNo, LifecycleTransformer<PayCodeBean> transformer){
        mRepository.payCode(tradeNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(payCodeBean -> {
                    if (payCodeBean.isSuccess()){
                        if (mView != null){
                            mView.onPaySuccess(payCodeBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onPayFile("支付失败：" + payCodeBean.getErr_code());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onPayFile(throwable.getMessage());
                    }
                });
    }

    /**
     * 支付回调轮训
     */
    public void payResult(String orderID , String userID, LifecycleTransformer<PayResultBean> transformer){
        mRepository.payResult(orderID, userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(payResultBean -> {
                    if (payResultBean.isSuccess()){
                        if (mView != null){
                            mView.onPayResultSuc(payResultBean);
                        }
                    }
                }, throwable -> {

                });
    }

}
