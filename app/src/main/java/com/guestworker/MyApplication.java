package com.guestworker;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.guestworker.netwrok.ApplicationComponent;
import com.guestworker.netwrok.DaggerApplicationComponent;
import com.guestworker.util.DebugHelper;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

/**
 * @author 莫小婷
 * @create 2019/4/9
 * @Describe
 */
public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mApplicationComponent =
                DaggerApplicationComponent
                        .create();
        Utils.init(instance);
        DebugHelper.syncIsDebug(this);
        //初始化友盟
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5cc030654ca357ce9a0001b9","umeng",UMConfigure.DEVICE_TYPE_PHONE, "");
        //微信
        PlatformConfig.setWeixin("wxfd80aece07456d53", "68f2c6ee2bab0f6e98f2f9ec547b2292");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }


    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
