package com.github.hyota.asciiartboardreader.di;

import android.content.Context;

import com.github.hyota.asciiartboardreader.BuildConfig;
import com.github.hyota.asciiartboardreader.di.annotation.Local;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class StorageModule {

    @Local
    @Provides
    File provideLocalStoragePath(Context context) {
        return context.getExternalFilesDir(null);
    }

}
