package com.guestworker.ui.activity.user.info;

import android.app.Dialog;

import com.blankj.utilcode.util.ToastUtils;
import com.guestworker.bean.UploadFileBean;
import com.guestworker.netwrok.Repository;
import com.guestworker.util.ToastUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author 莫小婷
 * @create 2019/5/13
 * @Describe
 */
public class InfoPresenter {

    private Repository mRepository;
    private InfoView mView;

    public void setView(InfoView view) {
        mView = view;
    }

    @Inject
    public InfoPresenter(Repository repository) {
        mRepository = repository;
    }

    /**
     * 上传图片
     */
    public void importCustomer(LifecycleTransformer<UploadFileBean> ransformer, Dialog mDialog, File file){
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        mRepository.uploadFile(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ransformer)
                .subscribe(imageBean -> {
                    if(imageBean != null){
                        if(mView != null){
                            mView.uploadPicSuccess(mDialog,imageBean.getFilepath());
                        }
                    }else {
                        if(mDialog != null && mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                        ToastUtil.show("上传图片失败");
                    }

                }, throwable -> {
                    if(mDialog != null && mDialog.isShowing()){
                        mDialog.dismiss();
                    }
                    ToastUtil.show("上传图片失败");
                });
    }
}
