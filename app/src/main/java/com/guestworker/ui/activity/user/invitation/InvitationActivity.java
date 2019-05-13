package com.guestworker.ui.activity.user.invitation;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.guestworker.R;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.InvitationBean;
import com.guestworker.databinding.ActivityInvitationBinding;
import com.guestworker.util.QRCodeUtil;
import com.guestworker.util.ToastUtil;
import com.guestworker.util.share.ShareUtils;
import com.guestworker.view.dialog.ProgressDialogView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/5/13
 * @Describe 邀请好友
 */
public class InvitationActivity extends BaseActivity implements View.OnClickListener, InvitationView {

    @Inject
    InvitationPresenter mPresenter;
    private ActivityInvitationBinding mBinding;
    private Dialog mDialog;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_invitation);
        mBinding.setListener(this);
        mBaseActivityComponent.inject(this);
        mPresenter.setView(this);
        mDialog = (new ProgressDialogView()).createLoadingDialog(this, "");
        mDialog.show();
        mPresenter.salescode(this.bindToLifecycle());
        mBinding.inClude.titleTv.setText("邀请好友");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.mine_copy:
                ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("kegong", mBinding.mineCode.getText().toString().replace("邀请码：",""));
                clip.setPrimaryClip(clipData);
                ToastUtil.show("复制成功");
                break;
            case R.id.invitation_share:
                if (bitmap == null){
                    ToastUtil.show("二维码生成失败无法分享");
                    return;
                }
                new ShareUtils(this, SHARE_MEDIA.WEIXIN)
                        .shareImage(this,bitmap);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        UMShareAPI.get(this).release();//分享防止内存泄露
        super.onDestroy();
    }

    @Override
    public void onSuccess(InvitationBean bean) {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        mBinding.mineCode.setText("邀请码：" + bean.getSalesCode());
        int widthPix = (int) getResources().getDimension(R.dimen.x242);
        int heightPix = (int) getResources().getDimension(R.dimen.y242);
        bitmap =  QRCodeUtil.createQRImage(bean.getUrl(),widthPix,heightPix,null);
        mBinding.invitationCode.setImageBitmap(bitmap);
    }

    @Override
    public void onFile() {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        ToastUtil.show("信息请求失败");
    }
}
