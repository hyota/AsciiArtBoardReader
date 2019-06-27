package com.github.hyota.asciiartboardreader.di;

import android.content.Context;

import com.github.hyota.asciiartboardreader.MyApplication;

import dagger.Module;

@Module
public class ApplicationModule {

    Context provideContext(MyApplication application) {
        return application.getApplicationContext();
    }

}
