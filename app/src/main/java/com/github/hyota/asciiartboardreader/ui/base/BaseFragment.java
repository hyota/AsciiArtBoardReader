package com.github.hyota.asciiartboardreader.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.ui.common.AlertDialogFragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public abstract class BaseFragment<VM extends ViewModel> extends Fragment implements AlertDialogFragment.Callback {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (hasActionBar != null) {
            hasActionBar.setToolbarTitle(getActionBarTitle());
        }
        initializeFloatingActionButton();
    }

    @NonNull
    protected abstract String getActionBarTitle();

    @NonNull
    protected abstract Class<VM> getViewModelClass();

    @Override
    public void onAlertDialogSucceeded(int requestCode, int resultCode, Bundle params) {
        // NOOP
    }

    @Override
    public void onAlertDialogCancelled(int requestCode, Bundle params) {
        // NOOP
    }

    protected void initializeFloatingActionButton() {
        if (hasFloatingActionButton != null) {
            hasFloatingActionButton.hideFloatingActionButton();
            hasFloatingActionButton.setFloatingActionButtonOnClickListener(null);
        }
    }

    protected void showErrorDialog(@NonNull String message) {
        new AlertDialogFragment.Builder(this)
                .setTitle(R.string.error_dialog_title)
                .setMessage(message)
                .setCancelable(true)
                .show();
    }

    protected void showErrorToast(@StringRes int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    protected void showErrorToast(@NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
