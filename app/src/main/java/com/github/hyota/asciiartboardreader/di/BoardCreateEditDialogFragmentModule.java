package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardCreateEditContract;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardCreateEditDialogFragment;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BoardCreateEditPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class BoardCreateEditDialogFragmentModule {

    @DialogFragmentScope
    @Provides
    BoardCreateEditContract.View provideView(BoardCreateEditDialogFragment view) {
        return view;
    }

    @DialogFragmentScope
    @Provides
    BoardCreateEditContract.Presenter providePresenter(BoardCreateEditPresenterImpl presenter) {
        return presenter;
    }

}
