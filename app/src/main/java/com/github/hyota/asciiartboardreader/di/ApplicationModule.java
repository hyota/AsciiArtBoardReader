package com.github.hyota.asciiartboardreader.di;

import android.content.Context;

import com.github.hyota.asciiartboardreader.MyApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    Context provideContext(MyApplication application) {
        return application.getApplicationContext();
    }

}
