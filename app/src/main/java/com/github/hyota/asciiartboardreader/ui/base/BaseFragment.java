package com.github.hyota.asciiartboardreader.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.model.value.AlertDialogRequestCodeValue;
import com.github.hyota.asciiartboardreader.model.value.ErrorDisplayTypeValue;
import com.github.hyota.asciiartboardreader.ui.common.AlertDialogFragment;
import com.github.hyota.asciiartboardreader.ui.common.ErrorMessageModel;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment implements AlertDialogFragment.Callback {

    @Inject
    ViewModelProvider.Factory factory;

    protected Context context;
    protected VM viewModel;

    @Nullable
    protected HasFloatingActionButton hasFloatingActionButton;
    @Nullable
    protected HasActionBar hasActionBar;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        viewModel = new ViewModelProvider(this, factory).get(getViewModelClass());
        this.context = context;
        if (context instanceof HasActionBar) {
            hasActionBar = (HasActionBar) context;
        }
        if (context instanceof HasFloatingActionButton) {
            hasFloatingActionButton = (HasFloatingActionButton) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessageModel -> {
            if (errorMessageModel != null) {
                showErrorMessage(errorMessageModel);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (hasActionBar != null) {
            hasActionBar.setToolbarTitle(getActionBarTitle());
        }
        initializeFloatingActionButton();
    }

    @Override
    public void onAlertDialogSucceeded(@AlertDialogRequestCodeValue.AlertDialogRequestCode int requestCode, int resultCode, Bundle params) {
        // NOOP
    }

    @Override
    public void onAlertDialogCancelled(@AlertDialogRequestCodeValue.AlertDialogRequestCode int requestCode, Bundle params) {
        // NOOP
    }

    @NonNull
    protected abstract String getActionBarTitle();

    @NonNull
    protected abstract Class<VM> getViewModelClass();

    protected void initializeFloatingActionButton() {
        if (hasFloatingActionButton != null) {
            hasFloatingActionButton.hideFloatingActionButton();
            hasFloatingActionButton.setFloatingActionButtonOnClickListener(null);
        }
    }

    protected void showErrorMessage(@NonNull ErrorMessageModel errorMessageModel) {
        String message = getString(errorMessageModel.getResId(), errorMessageModel.getParams());
        if (errorMessageModel.getDisplayType() == ErrorDisplayTypeValue.TOAST) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else if (errorMessageModel.getDisplayType() == ErrorDisplayTypeValue.DIALOG) {
            new AlertDialogFragment.Builder(this)
                    .setTitle(R.string.error_dialog_title)
                    .setMessage(message)
                    .setCancelable(true)
                    .show();
        } else {
            Timber.w("%s is disabled display type", errorMessageModel.getDisplayType());
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        viewModel.clearErrorMessage();
    }

}
