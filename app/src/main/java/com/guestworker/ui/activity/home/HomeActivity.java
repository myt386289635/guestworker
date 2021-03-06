package com.guestworker.ui.activity.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.guestworker.R;
import com.guestworker.adapter.HomeAdapter;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.eventbus.CartpageBus;
import com.guestworker.databinding.ActivityMainBinding;
import com.guestworker.ui.fragment.classify.ClassifyFragment;
import com.guestworker.ui.fragment.mine.MineFragment;
import com.guestworker.ui.fragment.shoppingcart.ShoppingCartFragment;
import com.guestworker.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends BaseActivity{

    @Inject
    HomePresenter mPresenter;
    private ActivityMainBinding mBinding;
    private List<Fragment> mFragments;
    private HomeAdapter mHomeAdapter;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mBaseActivityComponent.inject(this);
        mImmersionBar.reset()
                .transparentStatusBar()
                .statusBarDarkFont(true, 0f)
                .init();
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mBinding.viewPager.setNoScroll(true);
        mFragments = new ArrayList<>();
        mFragments.add(ClassifyFragment.getInstance());
        mFragments.add(ShoppingCartFragment.getInstance());
        mFragments.add(MineFragment.getInstance());
        mHomeAdapter = new HomeAdapter(getSupportFragmentManager());
        mHomeAdapter.setFragments(mFragments);
        mBinding.viewPager.setAdapter(mHomeAdapter);
        mBinding.viewPager.setOffscreenPageLimit(mFragments.size() - 1);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        mPresenter.initTabLayout(this, mBinding.tabLayout);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.show("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void cartpage(CartpageBus bus){
        if (bus != null){
            mBinding.viewPager.setCurrentItem(1);
        }
    }
}
