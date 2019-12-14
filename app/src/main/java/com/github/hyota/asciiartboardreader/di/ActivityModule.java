package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.di.scope.ActivityScope;
import com.github.hyota.asciiartboardreader.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Activityのコンポーネント作成.
 */
@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    abstract MainActivity contributeMainActiviy();

}
