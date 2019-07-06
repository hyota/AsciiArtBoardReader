package com.github.hyota.asciiartboardreader;

import com.facebook.stetho.Stetho;
import com.github.hyota.asciiartboardreader.di.DaggerApplicationComponent;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * アプリケーション.
 */
public class MyApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setFontAttrId(R.attr.fontPath).build());
        Iconify.with(new FontAwesomeModule());
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.factory().create(this);
    }

}
