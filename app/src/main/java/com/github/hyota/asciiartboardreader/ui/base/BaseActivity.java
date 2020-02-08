package com.github.hyota.asciiartboardreader.ui.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public abstract class BaseActivity<VM extends ViewModel> extends AppCompatActivity implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;
    @Inject
    ViewModelProvider.Factory factory;

    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, factory).get(getViewModelClass());
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }

    @NonNull
    protected abstract Class<VM> getViewModelClass();

}
