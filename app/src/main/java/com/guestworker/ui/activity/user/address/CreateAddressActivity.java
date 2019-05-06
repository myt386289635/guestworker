package com.guestworker.ui.activity.user.address;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.guestworker.R;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.AddressBean;
import com.guestworker.databinding.ActivityAddressBinding;
import com.guestworker.util.ToastUtil;
import com.guestworker.view.dialog.ProgressDialogView;
import com.guestworker.view.dialog.city.CityPickerDialog;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/5/6
 * @Describe 添加地址
 */
public class CreateAddressActivity extends BaseActivity implements View.OnClickListener, CreateAddressView {

    private ActivityAddressBinding mBinding;
    private Dialog mDialog;
    private String cityCode;
    private CityPickerDialog cityPickerDialog;

    @Inject
    CreateAddressPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_address);
        mBinding.setListener(this);
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        initView();
    }

    private void initView() {
        mBinding.inClude.titleTv.setText("新增地址");

        showKeyboardDelayed(mBinding.editName);
        mBinding.editName.setSelection(mBinding.editName.getText().length());

        onEditCity();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_add:
                //保存
                if (TextUtils.isEmpty(mBinding.editName.getText().toString().trim())){
                    ToastUtil.show("收件人不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.editNumber.getText().toString().trim())){
                    ToastUtil.show("联系方式不能为空");
                    return;
                }
                if (mBinding.editNumber.getText().toString().trim().length() < 11){
                    ToastUtil.show("联系方式不正确，请检查");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.editAddress.getText().toString().trim())){
                    ToastUtil.show("详细地址不能为空");
                    return;
                }
                mDialog = (new ProgressDialogView()).createLoadingDialog(this, "正在请求...");
                mDialog.show();
                //新增地址
                mPresenter.addressAdd(mBinding.editName.getText().toString(),
                        mBinding.editNumber.getText().toString(),
                        mBinding.editAddress.getText().toString(),
                        cityCode,getIntent().getIntExtra("userid",0) + "", this.bindToLifecycle());

                break;
            case R.id.areaContainer:
                //选择收货地区
                cityPickerDialog.show();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void hideSoftInput() {
        if (mImm.isActive()) {
            // 如果开启
            mImm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
        }
    }


    @Override
    public void onSuccess(AddressBean bean) {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        Intent intent = new Intent();
        intent.putExtra("id",bean.getAddressId());
        intent.putExtra("address",mBinding.editArea.getText().toString().replace(" ","") +  mBinding.editAddress.getText().toString());
        setResult(333,intent);
        finish();
        ToastUtil.show("成功");
    }

    @Override
    public void onFile(String error) {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        ToastUtil.show(error);
    }

    /**
     * 选择城市
     */
    public void onEditCity() {
        cityPickerDialog = new CityPickerDialog(this);
        cityPickerDialog.setCallback(new CityPickerDialog.OnClickCallback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String province, String city, String county, String data) {
                String cityTxt;
                if (TextUtils.isEmpty(county)) {
                    cityTxt = province+" "+city;
                } else {
                    cityTxt = province+" "+city+" "+county;
                }
                cityCode = data;
                mBinding.editArea.setText(cityTxt);
            }
        });
    }
}
