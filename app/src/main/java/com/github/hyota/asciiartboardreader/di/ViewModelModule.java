package com.github.hyota.asciiartboardreader.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.hyota.asciiartboardreader.di.annotation.ViewModelKey;
import com.github.hyota.asciiartboardreader.ui.bbslist.BbsAddEditViewModel;
import com.github.hyota.asciiartboardreader.ui.bbslist.BbsListViewModel;
import com.github.hyota.asciiartboardreader.ui.bbsthredlist.BbsThreadListViewModel;
import com.github.hyota.asciiartboardreader.ui.main.MainViewModel;

import java.util.Map;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule {

    @Provides
    ViewModelProvider.Factory provideViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelFactory(providerMap);
    }

    @IntoMap
    @Provides
    @ViewModelKey(MainViewModel.class)
    ViewModel bindMainViewModel(MainViewModel viewModel) {
        return viewModel;
    }

    @IntoMap
    @Provides
    @ViewModelKey(BbsListViewModel.class)
    ViewModel bindBbsListViewModel(BbsListViewModel viewModel) {
        return viewModel;
    }

    @IntoMap
    @Provides
    @ViewModelKey(BbsAddEditViewModel.class)
    ViewModel bindBbsAddEditViewModel(BbsAddEditViewModel viewModel) {
        return viewModel;
    }

    @IntoMap
    @Provides
    @ViewModelKey(BbsThreadListViewModel.class)
    ViewModel bindBbsThreadListViewModel(BbsThreadListViewModel viewModel) {
        return viewModel;
    }

}
