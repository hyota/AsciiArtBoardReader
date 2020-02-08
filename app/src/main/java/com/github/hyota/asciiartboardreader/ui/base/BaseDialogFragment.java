package com.github.hyota.asciiartboardreader.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.hyota.asciiartboardreader.model.value.ErrorDisplayTypeValue;
import com.github.hyota.asciiartboardreader.ui.common.AlertDialogFragment;
import com.github.hyota.asciiartboardreader.ui.common.ErrorMessageModel;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public abstract class BaseDialogFragment<VM extends BaseViewModel> extends DialogFragment implements AlertDialogFragment.Callback {

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
        viewModel.getErrorMessage().observe(this, errorMessageModel -> {
            if (errorMessageModel != null) {
                showErrorMessage(errorMessageModel);
            }
        });
    }

    @Override
    public void onAlertDialogSucceeded(int requestCode, int resultCode, Bundle params) {
        // NOOP
    }

    @Override
    public void onAlertDialogCancelled(int requestCode, Bundle params) {
        // NOOP
    }

    @NonNull
    protected abstract Class<VM> getViewModelClass();

    protected void showErrorMessage(@NonNull ErrorMessageModel errorMessageModel) {
        String message = getString(errorMessageModel.getResId(), errorMessageModel.getParams());
        if (errorMessageModel.getDisplayType() == ErrorDisplayTypeValue.TOAST) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else if (errorMessageModel.getDisplayType() == ErrorDisplayTypeValue.DIALOG) {
            Timber.w("DialogFragment(%s) can't show error dialog.", this);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else {
            Timber.w("%s is disabled display type", errorMessageModel.getDisplayType());
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        viewModel.clearErrorMessage();
    }

}
