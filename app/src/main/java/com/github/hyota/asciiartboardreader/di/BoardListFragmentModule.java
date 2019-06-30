package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardCreateEditDialogFragment;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardListContract;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardListFragment;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardListPresenterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BoardListFragmentModule {

    @Binds
    @FragmentScope
    abstract BoardListContract.View provideView(BoardListFragment view);

    @Binds
    @FragmentScope
    abstract BoardListContract.Presenter providePresenter(BoardListPresenterImpl presenter);

}
