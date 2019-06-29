package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.main.MainActivity;
import com.github.hyota.asciiartboardreader.presentation.main.MainContract;
import com.github.hyota.asciiartboardreader.presentation.main.MainPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * MainActivityにインジェクトするインスタンス生成クラス.
 */
@Module
public class MainActivityModule {

    @ActivityScope
    @Provides
    MainContract.View provideView(MainActivity view) {
        return view;
    }

    @ActivityScope
    @Provides
    MainContract.Presenter providePresenter(MainPresenterImpl presenter) {
        return presenter;
    }

}
