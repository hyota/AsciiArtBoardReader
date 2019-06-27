package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.main.MainActivity;
import com.github.hyota.asciiartboardreader.presentation.main.MainPresenter;
import com.github.hyota.asciiartboardreader.presentation.main.MainPresenterImpl;
import com.github.hyota.asciiartboardreader.presentation.main.MainView;

import dagger.Module;
import dagger.Provides;

/**
 * MainActivityにインジェクトするインスタンス生成クラス.
 */
@Module
public class MainActivityModule {

    @ActivityScope
    @Provides
    MainView provideView(MainActivity view) {
        return view;
    }

    @ActivityScope
    @Provides
    MainPresenter providePresenter(MainPresenterImpl presenter) {
        return presenter;
    }

}
