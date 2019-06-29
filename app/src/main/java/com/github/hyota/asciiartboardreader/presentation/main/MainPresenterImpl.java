package com.github.hyota.asciiartboardreader.presentation.main;

import com.github.hyota.asciiartboardreader.di.ActivityScope;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@ActivityScope
public class MainPresenterImpl implements MainContract.Presenter {

    @Nonnull
    private MainContract.View view;

    @Inject
    public MainPresenterImpl(@Nonnull MainContract.View view) {
        this.view = view;
    }

}
