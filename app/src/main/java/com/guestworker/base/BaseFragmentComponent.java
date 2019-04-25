package com.guestworker.base;

import com.guestworker.auto.FragmentScope;
import com.guestworker.netwrok.ApplicationComponent;
import com.guestworker.ui.fragment.classify.ClassifyFragment;
import com.guestworker.ui.fragment.mine.MineFragment;
import com.guestworker.ui.fragment.shoppingcart.ShoppingCartFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class)
public interface BaseFragmentComponent {
    void inject(ClassifyFragment fragment);
    void inject(MineFragment fragment);
    void inject(ShoppingCartFragment fragment);
}
