package com.guestworker.netwrok;

import com.guestworker.auto.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {
        RetrofitModule.class,
        OKHttpModule.class,
        ApplicationModule.class})
public interface ApplicationComponent {
   //父亲Component里module的方法需要暴露出来才能提供给子类Component使用
   APIService apiService();
}
