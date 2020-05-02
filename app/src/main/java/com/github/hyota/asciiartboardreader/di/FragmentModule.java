package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.di.annotation.FragmentScope;
import com.github.hyota.asciiartboardreader.ui.bbslist.BbsAddEditDialogFragment;
import com.github.hyota.asciiartboardreader.ui.bbslist.BbsListFragment;
import com.github.hyota.asciiartboardreader.ui.bbsthredlist.BbsThreadListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector(modules = {})
    @FragmentScope
    abstract BbsListFragment contributeBbsListFragment();

    @ContributesAndroidInjector(modules = {})
    @FragmentScope
    abstract BbsAddEditDialogFragment.BbsAddDialogFragment contributeBbsAddDialogFragment();

    @ContributesAndroidInjector(modules = {})
    @FragmentScope
    abstract BbsAddEditDialogFragment.BbsEditDialogFragment contributeBbsEditDialogFragment();

    @ContributesAndroidInjector(modules = {})
    @FragmentScope
    abstract BbsThreadListFragment contributeBbsThreadListFragment();

}
