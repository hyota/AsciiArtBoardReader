package com.github.hyota.asciiartboardreader.presentation.main;

import com.github.hyota.asciiartboardreader.di.ActivityScope;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@ActivityScope
public class MainPresenterImpl implements MainPresenter {

    @Nonnull
    private MainView view;

    @Inject
    public MainPresenterImpl(@Nonnull MainView view) {
        this.view = view;
    }

}
