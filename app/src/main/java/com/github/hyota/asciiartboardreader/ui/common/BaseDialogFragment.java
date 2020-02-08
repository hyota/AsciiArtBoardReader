package com.github.hyota.asciiartboardreader.ui.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public abstract class BaseDialogFragment<VM extends ViewModel> extends DialogFragment {

    @Inject
    ViewModelProvider.Factory factory;

    protected VM viewModel;
    protected Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.context = context;
        viewModel = new ViewModelProvider(this, factory).get(getViewModelClass());
    }

    @NonNull
    protected abstract Class<VM> getViewModelClass();


}
