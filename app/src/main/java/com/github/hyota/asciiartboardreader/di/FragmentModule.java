package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.di.scope.FragmentScope;
import com.github.hyota.asciiartboardreader.ui.bbslist.BbsListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector(modules = {})
    @FragmentScope
    abstract BbsListFragment contributeBbsListFragment();
}
