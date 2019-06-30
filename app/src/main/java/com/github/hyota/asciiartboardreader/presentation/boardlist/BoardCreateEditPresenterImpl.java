package com.github.hyota.asciiartboardreader.presentation.boardlist;

import com.github.hyota.asciiartboardreader.di.DialogFragmentScope;

import org.greenrobot.eventbus.EventBus;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@DialogFragmentScope
public class BoardCreateEditPresenterImpl implements BoardCreateEditContract.Presenter {

    @Nonnull
    private BoardCreateEditContract.View view;

    @Inject
    public BoardCreateEditPresenterImpl(@Nonnull BoardCreateEditContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void save(@Nonnull String url, @Nonnull String title) {

    }

    @Override
    public void save(int id, @Nonnull String url, @Nonnull String title) {

    }

    @Override
    public void loadTitleFromServer(@Nonnull String url) {

    }

    private boolean validete(@Nonnull String url, @Nonnull String title) {
        return false;
    }

    private boolean validete(int id, @Nonnull String url, @Nonnull String title) {
        return false;
    }

}
