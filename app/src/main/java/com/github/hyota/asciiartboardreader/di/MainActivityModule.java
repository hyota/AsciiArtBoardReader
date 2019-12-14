package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.di.scope.ActivityScope;
import com.github.hyota.asciiartboardreader.ui.main.MainActivity;
import com.github.hyota.asciiartboardreader.ui.main.MainContract;
import com.github.hyota.asciiartboardreader.ui.main.MainPresenterImpl;

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
