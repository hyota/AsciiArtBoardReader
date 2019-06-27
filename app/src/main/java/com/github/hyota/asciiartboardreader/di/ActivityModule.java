package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.main.MainActivity;

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
