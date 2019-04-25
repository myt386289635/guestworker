package com.guestworker.netwrok;

import android.content.Context;

import com.guestworker.auto.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Context mContext;

    ApplicationModule(Context context) {
        mContext = context;
    }

    @ApplicationScope
    @Provides
    Context provideContext() {
        return mContext;
    }
}
