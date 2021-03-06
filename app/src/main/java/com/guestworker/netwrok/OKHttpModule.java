package com.guestworker.netwrok;

import android.util.Log;

import com.guestworker.auto.ApplicationScope;
import com.guestworker.util.cookie.MyCookieJar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class OKHttpModule {

    public static final int CONNECT_TIME_OUT = 30;
    public static final int WRITE_TIME_OUT = 30;
    public static final int READ_TIME_OUT = 30;

    @ApplicationScope
    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder(SSLSocketFactory sslSocketFactory) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS).
                writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS).
                readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).
                retryOnConnectionFailure(true).
                sslSocketFactory(sslSocketFactory).
                addInterceptor(loggingInterceptor)
                .cookieJar(MyCookieJar.getInstance());
        return builder;
    }


    @ApplicationScope
    @Provides
    OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder) {
        builder.addInterceptor(chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);// 发送请求，获得回包
            // 对返回code统一拦截
            try {
                Charset charset;
                charset = Charset.forName("UTF-8");
                ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);//把内容copy了一边，所以不会有影响，peekBody() 方法返回的是一个新的 response 的 body
                Reader jsonReader = new InputStreamReader(responseBody.byteStream(), charset);
                BufferedReader reader = new BufferedReader(jsonReader);
                StringBuilder sbJson = new StringBuilder();
                String line = reader.readLine();
                do {
                    sbJson.append(line);
                    line = reader.readLine();
                } while (line != null);
                Log.e("OKHttpModule", sbJson.toString());// 输出返回结果
//                IsLoginBean bean = new Gson().fromJson(sbJson.toString(),IsLoginBean.class);
//                if(!bean.isSuccess() && bean.getMsg().equals("请先登录!")){
//                    MyApplication.getInstance().startActivity(new Intent(MyApplication.getInstance(),LoginActivity.class));
//                    SPUtils.getInstance(CommonDate.USER).clear();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        });
        return builder.build();
    }


    @ApplicationScope
    @Provides
    SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

}
