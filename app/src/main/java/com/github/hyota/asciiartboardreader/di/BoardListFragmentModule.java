package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardListContract;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardListFragment;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class BoardListFragmentModule {

    @FragmentScope
    @Provides
    BoardListContract.View provideView(BoardListFragment view) {
        return view;
    }

    @FragmentScope
    @Provides
    BoardListContract.Presenter providePresenter(BoardListPresenterImpl presenter) {
        return presenter;
    }

}
