package com.guestworker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.guestworker.netwrok.ApplicationComponent;
import com.guestworker.netwrok.DaggerApplicationComponent;
import com.guestworker.util.DebugHelper;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/9
 * @Describe
 */
public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    private static MyApplication instance;
    private List<Activity> activityList = new LinkedList<Activity>();

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

        initBugly();
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

    //添加Activity到容器中
    public void addActivity(Activity activity)  {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for(Activity activity:activityList) {
            if (activity != null){
                activity.finish();
            }
        }
        activityList.clear();
    }

    /**
     * 初始化bugly
     */
    private void initBugly(){
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setBuglyLogUpload(processName == null || processName.equals(packageName));
        if(DebugHelper.getDebug()){
            strategy.setAppVersion("开发版本");
        }
        // 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), "9f3a3a8257", false,strategy);
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
