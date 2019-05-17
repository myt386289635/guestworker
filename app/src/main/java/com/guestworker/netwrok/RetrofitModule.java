package com.guestworker.netwrok;
import com.guestworker.auto.ApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    public static final String URL = "http://api.eletest.eascs.com";//正式地址
    public static final String UP_BASE_URL = URL;
    public static String IMG_URL = "";//图片地址（需要从接口里返回）
    public static final String pageSize = "10";
    public static final String share_url = "https://eletest.eascs.com";

    @ApplicationScope
    @Provides
    RxJava2CallAdapterFactory provideRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @ApplicationScope
    @Provides
    GsonConverterFactory provideGsonConvertFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @ApplicationScope
    Retrofit provideRetrofit(OkHttpClient client, RxJava2CallAdapterFactory adapter, GsonConverterFactory converter) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(UP_BASE_URL)
                .client(client)
                .addConverterFactory(converter)
                .addCallAdapterFactory(adapter);
        return builder.build();
    }


    @Provides
    @ApplicationScope
    APIService provideUpService(Retrofit retrofit) {
        return retrofit.create(APIService.class);
    }

}
