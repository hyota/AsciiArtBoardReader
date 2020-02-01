package com.github.hyota.asciiartboardreader.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.ui.common.CommonDialogFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public abstract class BaseFragment extends Fragment implements CommonDialogFragment.Callback {

    protected Context context;
    private Unbinder unbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    public void onCommonDialogSucceeded(int requestCode, int resultCode, Bundle params) {
        // NOOP
    }

    @Override
    public void onCommonDialogCancelled(int requestCode, Bundle params) {
        // NOOP
    }

    protected void showErrorDialog(@NonNull String message) {
        new CommonDialogFragment.Builder(this)
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
