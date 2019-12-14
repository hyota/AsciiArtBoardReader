package com.github.hyota.asciiartboardreader.ui.main;

import com.github.hyota.asciiartboardreader.di.scope.ActivityScope;

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
