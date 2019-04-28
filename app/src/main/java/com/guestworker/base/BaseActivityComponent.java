package com.guestworker.base;

import com.guestworker.ui.activity.confirm.ConfirmActivity;
import com.guestworker.ui.activity.detail.DetailActivity;
import com.guestworker.ui.activity.home.HomeActivity;
import com.guestworker.auto.ActivityScope;
import com.guestworker.netwrok.ApplicationComponent;
import com.guestworker.ui.activity.login.LoginActivity;
import com.guestworker.ui.activity.shoplist.ListActivity;
import com.guestworker.ui.activity.user.UserActivity;
import com.guestworker.ui.activity.user.areaMembers.AreaUserActivity;
import com.guestworker.ui.activity.wellcome.WellComeActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface BaseActivityComponent {
    void inject(HomeActivity activity);
    void inject(ListActivity activity);
    void inject(DetailActivity activity);
    void inject(LoginActivity activity);
    void inject(UserActivity activity);
    void inject(AreaUserActivity activity);
    void inject(ConfirmActivity activity);
    void inject(WellComeActivity activity);
}
