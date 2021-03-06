package com.guestworker.util.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.guestworker.R;
import com.guestworker.util.ToastUtil;
import com.guestworker.view.dialog.ProgressDialogView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * @author 莫小婷
 * @create 2018/8/14
 * @Describe
 */
public class ShareUtils {


    private Context mContext;
    private SHARE_MEDIA mSHARE_media;
    private Dialog mDialog;

    public ShareUtils(Context context, SHARE_MEDIA SHARE_media) {
        mContext = context;
        mSHARE_media = SHARE_media;
        mDialog = (new ProgressDialogView()).createLoadingDialog(mContext, "");
    }

    /**
     * 分享链接
     */
    public void shareWeb(final Activity activity, String WebUrl, String title, String description, String imageUrl, int imageID) {
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(imageUrl)) {
            web.setThumb(new UMImage(activity, imageID));  //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imageUrl));  //网络缩略图
        }
        new ShareAction(activity)
                .setPlatform(mSHARE_media)//传入平台
                .withMedia(web)
                .setCallback(shareListener)//分享回调
                .share();
    }

    /**
     * 分享图片
     */
    public void shareImage(final Activity activity, Bitmap imageUrl){
        UMImage image = new UMImage(activity, imageUrl);//网络图片
        image.setThumb(new UMImage(activity, R.mipmap.logo));  //本地缩略图
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
        new ShareAction(activity)
                .setPlatform(mSHARE_media)//传入平台
                .withText(activity.getResources().getString(R.string.app_name))
                .withMedia(image)
                .setCallback(shareListener)//分享回调
                .share();
    }


    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            if(mDialog != null && !mDialog.isShowing()){
                mDialog.show();
            }
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
            ToastUtil.show("分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
            ToastUtil.show("分享失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if(mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
            ToastUtil.show("分享取消");
        }
    };

}
