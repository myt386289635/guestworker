package com.guestworker.ui.fragment.shoppingcart;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.guestworker.R;
import com.guestworker.adapter.CartAdapter;
import com.guestworker.base.BaseFragment;
import com.guestworker.bean.AreaUserBean;
import com.guestworker.bean.CartBean;
import com.guestworker.bean.OrderBean;
import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;
import com.guestworker.bean.PayResultBean;
import com.guestworker.bean.ShoppingCartBean;
import com.guestworker.bean.eventbus.AddCartBus;
import com.guestworker.bean.eventbus.RefreshCartBus;
import com.guestworker.bean.eventbus.Remarkbus;
import com.guestworker.databinding.FragmentCartBinding;
import com.guestworker.ui.activity.confirm.remark.RemarkActivity;
import com.guestworker.ui.activity.user.areaMembers.AreaUserActivity;
import com.guestworker.util.FileManager;
import com.guestworker.util.QRCodeUtil;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.WeakRefHandler;
import com.guestworker.util.cookie.GsonUtils;
import com.guestworker.util.permission.HasPermissionsUtil;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.dialog.DialogUtil;
import com.guestworker.view.dialog.ProgressDialogView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/9
 * @Describe
 */
public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener, OnRefreshListener, CartAdapter.OnItemClick, ShoppingCartView {

    private FragmentCartBinding mBinding;
    @Inject
    ShoppingCartPresenter mPresenter;
    private CartAdapter mAdapter;
    private List<ShoppingCartBean> mList;
    private Map<String,CartBean> mCartDate;
    private Boolean flag = false;//是否全选
    private Dialog mDialog;
    private AreaUserBean.AreaMemberListBean mMemberBean;
    private Boolean once = true;//记录第一次进入页面标示
    private String orderID = "";
    private Dialog payDialog;
    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 100){
                if (!TextUtils.isEmpty(orderID)){
                    mPresenter.payResult(orderID,mMemberBean.getUserid() + "",ShoppingCartFragment.this.bindUntilEvent(FragmentEvent.DESTROY_VIEW));
                    mHandler.sendEmptyMessageDelayed(100,1000);
                }
            }
            return true;
        }
    };
    private Handler mHandler = new WeakRefHandler(mCallback, Looper.getMainLooper());
    private IWXAPI mMsgApi;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && once) {
            mBinding.swipeToLoad.setRefreshing(true);
            mBinding.swipeToLoad.setRefreshEnabled(false);
        }
    }

    public static ShoppingCartFragment getInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        return fragment;
    }

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart,container,false);
        mBinding.setListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void initView() {
        mBaseFragmentComponent.inject(this);
        mPresenter.setView(this);
        mPresenter.setStatusBarHight(mBinding.statusBar,getContext());
        EventBus.getDefault().register(this);
        mMsgApi = WXAPIFactory.createWXAPI(getContext(), null);
        mMsgApi.registerApp("wxfd80aece07456d53");

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mList,getContext());
        mAdapter.setOnItemClick(this);
        mBinding.recyclerView.setAdapter(mAdapter);

        mBinding.swipeToLoad.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cart_edit:
                mPresenter.initCartBuy(mBinding);
                break;
            case R.id.cart_buy:
                if (mBinding.cartBuy.getText().toString().equals("前往支付")){
                    //前往支付
                    if (mMemberBean == null){
                        ToastUtil.show("请选择购买用户");
                        return;
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).getSelect()){
                            break;
                        } else if (i == mList.size() - 1) {
                            ToastUtil.show("请选择商品");
                            return;
                        }
                    }
                    mDialog = (new ProgressDialogView()).createLoadingDialog(getContext(), "");
                    mDialog.show();
                    OrderBean bean = new OrderBean();
                    List<OrderBean.GoodsInfoBean> goodsInfoBeans = new ArrayList<>();
                    for (ShoppingCartBean shoppingCartBean : mList) {
                        if (shoppingCartBean.getSelect()){
                            OrderBean.GoodsInfoBean goodsInfoBean = new OrderBean.GoodsInfoBean();
                            goodsInfoBean.setGoodsCount(shoppingCartBean.getCartBean().getNum());
                            goodsInfoBean.setGoodsID(shoppingCartBean.getCartBean().getBean().getDefaultgoods().getGid());
                            goodsInfoBeans.add(goodsInfoBean);
                        }
                    }
                    bean.setGoodsInfo(goodsInfoBeans);
                    bean.setAddressID(mMemberBean.getUaid());
                    bean.setUserID(mMemberBean.getUserid());
                    mPresenter.orderSave(bean,"APP",this.bindToLifecycle());
                }else {
                    //删除
                    for (int i = mList.size() - 1; i >= 0; i--) {
                        if (mList.get(i).getSelect()){
                            mCartDate.remove(mList.get(i).getCartBean().getBean().getDefaultgoods().getGid());
                            mList.remove(i);
                        }
                    }
                    if (mList.size() != 0){
                        mAdapter.notifyDataSetChanged();
                    }else {
                        mPresenter.initError(mBinding);
                        flag = false;
                        mBinding.cartSelect.setImageResource(R.mipmap.cart_nor);
                    }
                    String jsonStr = GsonUtils.toJson(mCartDate, new TypeToken<Map<String, CartBean>>(){});
                    SPUtils.getInstance(CommonDate.CART).put(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE, ""),jsonStr);
                    mBinding.confirmMoney.setText("¥0");
                }
                break;
            case R.id.cart_pay:
                //生成付款码
                if (mMemberBean == null){
                    ToastUtil.show("请选择购买用户");
                    return;
                }
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getSelect()){
                        break;
                    } else if (i == mList.size() - 1) {
                        ToastUtil.show("请选择商品");
                        return;
                    }
                }
                mDialog = (new ProgressDialogView()).createLoadingDialog(getContext(), "");
                mDialog.show();
                OrderBean bean = new OrderBean();
                List<OrderBean.GoodsInfoBean> goodsInfoBeans = new ArrayList<>();
                for (ShoppingCartBean shoppingCartBean : mList) {
                    if (shoppingCartBean.getSelect()){
                        OrderBean.GoodsInfoBean goodsInfoBean = new OrderBean.GoodsInfoBean();
                        goodsInfoBean.setGoodsCount(shoppingCartBean.getCartBean().getNum());
                        goodsInfoBean.setGoodsID(shoppingCartBean.getCartBean().getBean().getDefaultgoods().getGid());
                        goodsInfoBeans.add(goodsInfoBean);
                    }
                }
                bean.setGoodsInfo(goodsInfoBeans);
                bean.setAddressID(mMemberBean.getUaid());
                bean.setUserID(mMemberBean.getUserid());
                mPresenter.orderSave(bean,"NATIVE",this.bindToLifecycle());
                break;
            case R.id.cart_selectContainer:
                //全选
                if (flag){
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setSelect(false);
                    }
                    mAdapter.notifyDataSetChanged();
                    flag = false;
                    mBinding.cartSelect.setImageResource(R.mipmap.cart_nor);
                    mBinding.confirmMoney.setText("¥0");
                }else {
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setSelect(true);
                    }
                    mAdapter.notifyDataSetChanged();
                    flag = true;
                    mBinding.cartSelect.setImageResource(R.mipmap.cart_sel);
                    mPresenter.getTotlePrice(mList,mBinding,-1,false);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        dissRefresh();
        if (SPUtils.getInstance(CommonDate.USER).getBoolean(CommonDate.LOGIN)){
            String cartDate = SPUtils.getInstance(CommonDate.CART).getString(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE, ""), "");
            mCartDate = GsonUtils.fromJson(cartDate, new TypeToken<Map<String, CartBean>>(){});
            if (mCartDate == null || mCartDate.isEmpty()) {
                //没有数据
                mPresenter.initError(mBinding);
            }else {
                //有数据
                initDate();
                mBinding.cartBottom.setVisibility(View.VISIBLE);
                mBinding.cartEdit.setVisibility(View.VISIBLE);
                mBinding.recyclerView.setVisibility(View.VISIBLE);
                mBinding.netClude.errorContainer.setVisibility(View.GONE);
                mList.clear();
                for (String key : mCartDate.keySet()) {
                    mList.add(new ShoppingCartBean(false,mCartDate.get(key)));
                }
                mAdapter.notifyDataSetChanged();
                if (once){
                    once = false;
                }
            }
        }else {
            mPresenter.initError(mBinding);
        }
    }

    public void dissRefresh(){
        if (mBinding.swipeToLoad != null && mBinding.swipeToLoad.isRefreshing()) {
            mBinding.swipeToLoad.setRefreshEnabled(true);
            mBinding.swipeToLoad.setRefreshing(false);
            mBinding.swipeToLoad.setRefreshEnabled(false);
        }
    }

    /**
     * 减少
     */
    @Override
    public void onDecrease(int position) {
        if (mList.get(position).getCartBean().getNum() == 1){
            ToastUtil.show("已经是最小购买数量了");
            return;
        }
        commonIncDec(position,-1);
        if (mList.get(position).getSelect()){
            //选中时加减需要更新价格
            mPresenter.getTotle(mList,mBinding,position,false);
        }
    }

    /**
     * 增加
     */
    @Override
    public void onIncrease(int position) {
        if (mList.get(position).getCartBean().getNum() >= mList.get(position).getCartBean().getBean().getDefaultgoods().getStock()){
            ToastUtil.show("已经是最大购买数量了");
            return;
        }
        commonIncDec(position,1);
        if (mList.get(position).getSelect()){
            //选中时加减需要更新价格
            mPresenter.getTotle(mList,mBinding,position,true);
        }
    }

    public void commonIncDec(int position,int sum){
        int num = mList.get(position).getCartBean().getNum();
        mList.get(position).getCartBean().setNum(num + sum);
        mAdapter.notifyDataSetChanged();
        mCartDate.get(mList.get(position).getCartBean().getBean().getDefaultgoods().getGid()).setNum(mList.get(position).getCartBean().getNum());
        String jsonStr = GsonUtils.toJson(mCartDate, new TypeToken<Map<String, CartBean>>(){});
        SPUtils.getInstance(CommonDate.CART).put(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE, ""),jsonStr);
    }

    /**
     * 点击item选择时
     */
    @Override
    public void onSelect(int position) {
        if (mList.get(position).getSelect()){
            //取消选择
            mList.get(position).setSelect(false);
            if (flag){
                flag = false;
                mBinding.cartSelect.setImageResource(R.mipmap.cart_nor);
            }
        }else {
            //添加选择
            mList.get(position).setSelect(true);
            for (int i = 0; i < mList.size(); i++) {
                if (!mList.get(i).getSelect()){
                    break;
                }else if (i == mList.size() - 1){
                    flag = true;
                    mBinding.cartSelect.setImageResource(R.mipmap.cart_sel);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        mPresenter.getTotlePrice(mList,mBinding,position,mList.get(position).getSelect());
    }

    /**
     * 跳转到选着用户
     */
    @Override
    public void onUserContainer() {
        startActivity(new Intent(getContext(), AreaUserActivity.class)
                .putExtra("title", "我的用户")
                .putExtra("isMineModel", false)
                .putExtra("isHome", true)
        );
    }

    @Override
    public void onRemark(String remark) {
        //备注
        if (mMemberBean == null){
            ToastUtil.show("请选择购买用户");
            return;
        }
        startActivity(new Intent(getContext(),RemarkActivity.class)
                .putExtra("isHome",true)
                .putExtra("content",remark)
        );
    }

    @Override
    public void onDiscount() {
        //优惠券
        if (mMemberBean == null){
            ToastUtil.show("请选择购买用户");
            return;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 商品详情点击购物车跳转过来需要刷新购物车
     */
    @Subscribe
    public void onRefreshCart(AddCartBus bus){
        if (bus != null){
            mBinding.swipeToLoad.setRefreshEnabled(true);
            mBinding.swipeToLoad.setRefreshing(true);
            mBinding.swipeToLoad.setRefreshEnabled(false);
        }
    }

    /**
     * 退出登陆或者登陆后，需要刷新一下购物车
     */
    @Subscribe
    public void onLoginCart(RefreshCartBus bus){
        if (bus != null ){
            mBinding.swipeToLoad.setRefreshEnabled(true);
            mBinding.swipeToLoad.setRefreshing(true);
            mBinding.swipeToLoad.setRefreshEnabled(false);
            mAdapter.setUser(false);//刷新用户选择框恢复原样
        }
    }

    /**
     * 选择用户后，返回带回数据
     */
    @Subscribe
    public void onRefreshUserContainer(AreaUserBean.AreaMemberListBean bean) {
        if (bean != null){
            //选择客户后返回
            mMemberBean = bean;
            mAdapter.setUser(true);
            mAdapter.setMemberListBean(bean);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加备注
     */
    @Subscribe
    public void onRemark(Remarkbus remarkbus){
        if (remarkbus != null){
            mAdapter.setRemark(remarkbus.getRemark());
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 恢复原来数据
     */
    public void initDate(){
        mPresenter.initDate(mBinding);
        flag = false;
    }

    /**
     * 下单接口回调
     */
    @Override
    public void onSuccess(OrderSaveBean bean,String type) {
        //订单生成成功
        if (type.equals("NATIVE")){
            orderID = bean.getOrderID();
        }
        mPresenter.payCode(bean.getOrderID(),type,this.bindToLifecycle());
    }

    @Override
    public void onFile(String error) {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        ToastUtil.show(error);
    }

    /**
     * 支付二维码回调
     */
    @Override
    public void onPaySuccess(PayCodeBean bean,String tradeType) {
        if (tradeType.equals("NATIVE")){
            //打开轮训
            mHandler.sendEmptyMessage(100);
            int widthPix = (int) getContext().getResources().getDimension(R.dimen.x242);
            int heightPix = (int) getContext().getResources().getDimension(R.dimen.y242);
            Bitmap bitmap =  QRCodeUtil.createQRImage(bean.getData().getCode_url(),widthPix,heightPix,null);
            payDialog = DialogUtil.payDialog(getContext(), bitmap, v12 -> {
                orderID = "";
                mHandler.removeCallbacksAndMessages(null);
                saveImageToGallery(bitmap);
            }, dialog -> {
                DialogUtil.LoginDialog(getContext(), "支付未完成，关闭后无法清空购物车", "确定", "取消", v -> {
                    orderID = "";
                    mHandler.removeCallbacksAndMessages(null);
                    if (payDialog != null && payDialog.isShowing()){
                        payDialog.dismiss();
                    }
                });
            });
        }else {
            //app
            DialogUtil.LoginDialog(getContext(), "“客工”想要打开“微信支付”", "打开", "取消", v1 -> {
                // TODO: 2019/4/20 前往支付
                PayReq request = new PayReq();
                request.appId = bean.getData().getAppId();
                request.partnerId = "1900000109";
                request.prepayId= "1101000000140415649af9fc314aa427";
                request.packageValue = "Sign=WXPay";
                request.nonceStr= "1101000000140429eb40476f8896f4c9";
                request.timeStamp= "1398746574";
                request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
                mMsgApi.sendReq(request);
            });
        }
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    @Override
    public void onPayFile(String error) {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        orderID = "";
        ToastUtil.show(error);
    }

    /**
     * 支付回调轮训成功
     */
    @Override
    public void onPayResultSuc(PayResultBean bean) {
        if (bean.getOrderInfo().getPaymentstatus() == 12){
            //支付成功
            orderID = "";
            mHandler.removeCallbacksAndMessages(null);
            if (payDialog != null && payDialog.isShowing()){
                payDialog.dismiss();
            }
            //暂无商品
            mPresenter.initError(mBinding);
            mCartDate.clear();
            String jsonStr = GsonUtils.toJson(mCartDate, new TypeToken<Map<String, CartBean>>(){});
            SPUtils.getInstance(CommonDate.CART).put(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE, ""),jsonStr);
            ToastUtil.show("支付成功");
        }
//        Log.d("moxiaoting", "bean.getOrderInfo().getPaymentstatus():" + bean.getOrderInfo().getPaymentstatus());
    }

    /**
     * 下载二维码
     */
    public void saveImageToGallery(Bitmap bitmap){
        HasPermissionsUtil.permission(getContext(),new HasPermissionsUtil() {
            //有权限了
            @Override
            public void hasPermissionsSuccess() {
                FileManager.saveBitmap(getContext(), bitmap);
                ToastUtil.show("保存成功");
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
