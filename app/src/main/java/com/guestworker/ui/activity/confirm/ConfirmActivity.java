package com.guestworker.ui.activity.confirm;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.guestworker.R;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.AreaUserBean;
import com.guestworker.bean.DetailBean;
import com.guestworker.bean.OrderBean;
import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;
import com.guestworker.databinding.ActivityConfirmBinding;
import com.guestworker.ui.activity.user.areaMembers.AreaUserActivity;
import com.guestworker.util.FileManager;
import com.guestworker.util.QRCodeUtil;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.permission.HasPermissionsUtil;
import com.guestworker.view.dialog.DialogUtil;
import com.guestworker.view.dialog.ProgressDialogView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/16
 * @Describe 确认订单
 */
public class ConfirmActivity extends BaseActivity implements View.OnClickListener, ConfirmView {

    @Inject
    ConfirmPresenter mPresenter;

    private ActivityConfirmBinding mBinding;
    private DetailBean mDetailBean;
    private Dialog mDialog;
    private AreaUserBean.AreaMemberListBean mBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_confirm);
        mDetailBean = (DetailBean) getIntent().getSerializableExtra("date");
        mBinding.setListener(this);
        mBinding.setDate(mDetailBean.getDefaultgoods());
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        initView();
    }

    private void initView() {
        mBinding.inClude.titleTv.setText("确认订单");
        mBinding.confirmConfirmUserContainer.setVisibility(View.GONE);
        mBinding.confirmUserContainer.setVisibility(View.VISIBLE);
        mBinding.confirmMoney.setText("¥" + mDetailBean.getDefaultgoods().getPrice());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.confirm_decrease:
                //  -
                BigDecimal bigDecimal = new BigDecimal(mBinding.confirmNum.getText().toString());
                int num = bigDecimal.intValue();
                if(num == 1){
                    ToastUtil.show("已经是最小购买数量了");
                    return;
                }
                if(num > 1){
                    mBinding.confirmNum.setText((num - 1) + "");
                }
                getTotle();
                break;
            case R.id.confirm_increase:
                //  +
                BigDecimal bigDecimal1 = new BigDecimal(mBinding.confirmNum.getText().toString());
                int num1 = bigDecimal1.intValue();
                if(num1 >= mDetailBean.getDefaultgoods().getStock()){
                    ToastUtil.show("已经是最大购买数量了");
                    return;
                }
                mBinding.confirmNum.setText((num1 + 1) + "");
                getTotle();
                break;
            case R.id.confirm_confirmUserContainer:
            case R.id.confirm_userContainer:
                //选择用户
                startActivityForResult(new Intent(this,AreaUserActivity.class)
                        .putExtra("title","我的用户")
                        .putExtra("isMineModel",false)
                ,201);
                break;
            case R.id.confirm_buy:
                //前往支付
                DialogUtil.LoginDialog(this, "“客工”想要打开“微信支付”", "打开", "取消", v1 -> {
                    // TODO: 2019/4/20 前往支付
                });
                break;
            case R.id.confirm_pay:
                //生成付款码
                if (mBean == null){
                    ToastUtil.show("请选择购买用户");
                    return;
                }
                if (mDetailBean == null){
                    ToastUtil.show("数据请求错误，请重新进入页面");
                    return;
                }
                mDialog = (new ProgressDialogView()).createLoadingDialog(this, "");
                mDialog.show();
                OrderBean bean = new OrderBean();
                List<OrderBean.GoodsInfoBean> goodsInfoBeans = new ArrayList<>();
                OrderBean.GoodsInfoBean goodsInfoBean = new OrderBean.GoodsInfoBean();
                BigDecimal number = new BigDecimal(mBinding.confirmNum.getText().toString());
                goodsInfoBean.setGoodsCount(number.intValue());
                goodsInfoBean.setGoodsID(mDetailBean.getDefaultgoods().getGid());
                goodsInfoBeans.add(goodsInfoBean);
                bean.setGoodsInfo(goodsInfoBeans);
                bean.setAddressID(mBean.getUaid());
                bean.setUserID(mBean.getUserid());
                mPresenter.orderSave(bean,this.bindToLifecycle());
                break;
        }
    }

    /**
     * 计算总数
     */
    public void getTotle(){
        BigDecimal price = new BigDecimal(mBinding.confirmPrice.getText().toString().replace("¥",""));
        BigDecimal newbigdecimal = new BigDecimal(mBinding.confirmNum.getText().toString());
        double totle = price.doubleValue() * newbigdecimal.intValue();
        BigDecimal code = new BigDecimal(totle);
        mBinding.confirmMoney.setText( "¥" + code.setScale(2,BigDecimal.ROUND_DOWN));
        mBinding.confirmNumber.setText( "x" + newbigdecimal.intValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 201:
                //选择客户后返回
                mBinding.confirmConfirmUserContainer.setVisibility(View.VISIBLE);
                mBinding.confirmUserContainer.setVisibility(View.GONE);
                mBean = (AreaUserBean.AreaMemberListBean) data.getSerializableExtra("date");
                if (mBean != null) mBinding.setUser(mBean);
                break;
        }
    }

    /**
     * 下载二维码
     */
    public void saveImageToGallery(Bitmap bitmap){
        HasPermissionsUtil.permission(this,new HasPermissionsUtil() {
            //有权限了
            @Override
            public void hasPermissionsSuccess() {
                FileManager.saveBitmap(ConfirmActivity.this, bitmap);
                ToastUtil.show("保存成功");
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onSuccess(OrderSaveBean bean) {
        //订单生成成功
        mPresenter.payCode(bean.getOrderID(),this.bindToLifecycle());
    }

    @Override
    public void onFile(String error) {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        ToastUtil.show(error);
    }

    @Override
    public void onPaySuccess(PayCodeBean bean) {
        int widthPix = (int) getResources().getDimension(R.dimen.x242);
        int heightPix = (int) getResources().getDimension(R.dimen.y242);
        Bitmap bitmap =  QRCodeUtil.createQRImage(bean.getData().getCode_url(),widthPix,heightPix,null);
        DialogUtil.payDialog(this, bitmap, v12 -> {
            saveImageToGallery(bitmap);
        }, dialog -> dialog.dismiss());
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    @Override
    public void onPayFile(String error) {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        ToastUtil.show(error);
    }
}
