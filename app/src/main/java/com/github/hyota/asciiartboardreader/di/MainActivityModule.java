package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardListFragment;
import com.github.hyota.asciiartboardreader.presentation.main.MainActivity;
import com.github.hyota.asciiartboardreader.presentation.main.MainContract;
import com.github.hyota.asciiartboardreader.presentation.main.MainPresenterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * MainActivityにインジェクトするインスタンス生成クラス.
 */
@Module
abstract class MainActivityModule {

    @ActivityScope
    @Binds
    abstract MainContract.View provideView(MainActivity view);

    @ActivityScope
    @Binds
    abstract MainContract.Presenter providePresenter(MainPresenterImpl presenter);

    @ContributesAndroidInjector(modules = {BoardListFragmentModule.class})
    @FragmentScope
    abstract BoardListFragment contributeBoardListFragment();

}
