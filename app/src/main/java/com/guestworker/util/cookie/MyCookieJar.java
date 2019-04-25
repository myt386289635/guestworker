package com.guestworker.util.cookie;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.guestworker.util.sp.CommonDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe 处理cookie
 */
public class MyCookieJar implements CookieJar {

    private static volatile MyCookieJar instance;
    private volatile Map<String, List<Cookie>> cookiesMap;

    public static MyCookieJar getInstance() {
        if (instance == null) {
            synchronized (MyCookieJar.class) {
                if (instance == null) {
                    instance = new MyCookieJar();
                    instance.load();
                }
            }
        }
        return instance;
    }

    private MyCookieJar() {
    }

    //初始化需要做的
    private void load() {
        String cookies = SPUtils.getInstance(CommonDate.USER).getString(CommonDate.COOKIE, "");
        cookiesMap = GsonUtils.fromJson(cookies, new TypeToken<Map<String, List<Cookie>>>(){});
        if (cookiesMap == null) { //说明手机里没有cookie值
            cookiesMap = new HashMap<>();
        }
    }

    public synchronized void removeAll() {
        if (cookiesMap == null) {
            return;
        }
        cookiesMap.clear();
        flush();
    }

    private void flush() {
        if (cookiesMap == null) {
            return;
        }
        SPUtils.getInstance(CommonDate.USER).remove(CommonDate.COOKIE);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies == null || cookies.size() == 0) {
            return;
        }
        for (Cookie cookie : cookies) {
            add(cookie, false);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookieList = new ArrayList<>();
//        String domain = url.host();
        synchronized (this) {
            if (cookiesMap.containsKey(CommonDate.COOKIE)) {
                cookieList.addAll(cookiesMap.get(CommonDate.COOKIE));
            }
        }
        return cookieList;
    }

    public synchronized void add(Cookie cookie, boolean needFlush) {
        if (cookie == null) {
            throw new NullPointerException("cookie == null");
        }
        addCookie(cookie);
        if (needFlush) {
            flush();
        }
    }

    private void addCookie(Cookie cookie) {
        if (cookie == null) {
            return;
        }
//        String host = cookie.domain();
        List<Cookie> cookieList = cookiesMap.get(CommonDate.COOKIE);
        if (cookieList == null) {
            cookieList = new ArrayList<>();
            cookiesMap.put(CommonDate.COOKIE, cookieList);
        }
        for (int i = 0; i < cookieList.size(); i ++) {
            if (cookieList.get(i).name().equals(cookie.name())) {
                cookieList.remove(i);
                break;
            }
        }
        cookieList.add(cookie);

        //保存cookie
        String jsonStr = GsonUtils.toJson(cookiesMap, new TypeToken<Map<String, List<Cookie>>>(){});
        SPUtils.getInstance(CommonDate.USER).put(CommonDate.COOKIE,jsonStr);
    }

}
