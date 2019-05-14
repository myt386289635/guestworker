package com.guestworker.ui.activity.user.info;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.guestworker.R;
import com.guestworker.base.BaseActivity;
import com.guestworker.databinding.ActivityInfoBinding;
import com.guestworker.netwrok.RetrofitModule;
import com.guestworker.util.ImageCompressUtil;
import com.guestworker.util.glide.GlideApp;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.dialog.ProgressDialogView;
import com.guestworker.view.dialog.SelectPicDialog;

import java.io.File;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/5/13
 * @Describe 个人资料
 */
public class InfoActivity extends BaseActivity implements View.OnClickListener, SelectPicDialog.ChoosePhotoCallback, InfoView {

    @Inject
    InfoPresenter mPresenter;
    private ActivityInfoBinding mBinding;
    private SelectPicDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_info);
        mBinding.setListener(this);
        initView();
    }

    private void initView() {
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        mBinding.inClude.titleTv.setText("个人资料");

        //初始化数据
        if (TextUtils.isEmpty(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.userheadpath,""))){
            GlideApp.loderImage(this,R.mipmap.default_img,mBinding.infoImage,0,0);
        }else {
            GlideApp.loderCircleImage(this,SPUtils.getInstance(CommonDate.USER).getString(CommonDate.userheadpath),mBinding.infoImage,R.mipmap.default_img,0);
        }
        if (TextUtils.isEmpty(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.NAME,""))){
            mBinding.infoName.setText(editPhone(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE)));
        }else {
            mBinding.infoName.setText(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.NAME));
        }
        mBinding.infoNumber.setText(SPUtils.getInstance(CommonDate.USER).getString(CommonDate.PHONE));
        GlideApp.loderImage(this,SPUtils.getInstance(CommonDate.USER).getString(CommonDate.buscardpic,""),mBinding.infoCodeImg,0,0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.info_imageContainer:

                break;
            case R.id.info_nameContainer:

                break;
            case R.id.info_numberContainer:

                break;
            case R.id.info_codeContainer:
                //微信二维码上传
                if(mDialog == null){
                    mDialog = new SelectPicDialog();
                    mDialog.setChoosePhotoCallback(this);
                }
                if(!mDialog.isAdded()){
                    mDialog.show(getSupportFragmentManager(), SelectPicDialog.TAG);
                }
                break;
        }
    }

    public String editPhone(String phone){
        if(!TextUtils.isEmpty(phone)){
            String start = phone.substring(0,3);
            String end = phone.substring(phone.length() - 4,phone.length());
            start = start + "****" + end;
            return start;
        }
        return phone;
    }

    @Override
    public void getAbsolutePicPath(String picFile) {
        Dialog mDialog = (new ProgressDialogView()).createLoadingDialog(this, "");
        mDialog.show();
        //这里进行了图片旋转以及图片压缩后得到新图片
        picFile = ImageCompressUtil.getimage(this,ImageCompressUtil.getPicture(this,picFile));
        mPresenter.importCustomer(this.bindToLifecycle(),mDialog,new File(picFile));
    }

    @Override
    public void uploadPicSuccess(Dialog mDialog, String picPath) {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        picPath = RetrofitModule.IMG_URL + picPath;
        SPUtils.getInstance(CommonDate.USER).put(CommonDate.buscardpic,picPath);
        GlideApp.loderImage(this,picPath,mBinding.infoCodeImg,0,0);
    }
}
