package com.guestworker.ui.activity.shoplist;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

import com.guestworker.R;
import com.guestworker.bean.ListBean;
import com.guestworker.netwrok.Repository;
import com.guestworker.netwrok.RetrofitModule;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 莫小婷
 * @create 2019/4/15
 * @Describe
 */
public class ListPresenter {

    private Repository mRepository;
    private ListView mView;

    public void setView(ListView view) {
        mView = view;
    }

    @Inject
    public ListPresenter(Repository repository) {
        mRepository = repository;
    }


    public void initText(Context context,int position ,List<TextView> mTextViews ){
        for (int i = 0; i < mTextViews.size(); i++) {
            if (i == position){
                //选中
                mTextViews.get(position).setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                mTextViews.get(position).setTextColor(context.getResources().getColor(R.color.color_DC3D27));
            }else {
                //没选中
                mTextViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                mTextViews.get(i).setTextColor(context.getResources().getColor(R.color.color_DE090909));
            }
        }
    }

    public void goodsList(String gtid, int currentPosition, int sort , String pageon,LifecycleTransformer<ListBean> transformer){
        Map<String,String> map = new HashMap<>();
        map.put("gtid",gtid);
        if (currentPosition == 1){
            map.put("orderby","salesvolumes");
        }else if (currentPosition == 2){
            map.put("orderby","putondate");
        }else if (currentPosition == 3){
            map.put("orderby","price");
            if (sort == 1){
                //降序
                map.put("sequence","desc");
            }else {
                //升序
                map.put("sequence","asc");
            }
        }
        map.put("row", RetrofitModule.pageSize);
        map.put("pageon",pageon);
        mRepository.goodsList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(listBean -> {
                    if (listBean.isSuccess()){
                        if (mView != null){
                            mView.onSuccess(listBean);
                        }
                    }else {
                        if (mView != null){
                            mView.onFile(listBean.getMsg());
                        }
                    }
                }, throwable -> {
                    if (mView != null){
                        mView.onFile(throwable.getMessage());
                    }
                });
    }


}
