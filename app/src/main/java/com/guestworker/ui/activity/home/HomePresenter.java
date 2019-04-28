package com.guestworker.ui.activity.home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.guestworker.R;
import com.guestworker.netwrok.Repository;

import javax.inject.Inject;

/**
 * @author 莫小婷
 * @create 2019/4/9
 * @Describe
 */
public class HomePresenter {

    private Repository mRepository;

    @Inject
    public HomePresenter(Repository repository) {
        mRepository = repository;
    }

    public void initTabLayout(Context context , TabLayout tabLayout){
        View view1 = LayoutInflater.from(context).inflate(R.layout.tablayout_item1,null);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.tablayout_item2,null);
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = LayoutInflater.from(context).inflate(R.layout.tablayout_item3,null);
        tabLayout.getTabAt(2).setCustomView(view3);

        //水波纹颜色
        tabLayout.setTabRippleColor(ColorStateList.valueOf(context.getResources().getColor(R.color.transparent)));
    }

}
