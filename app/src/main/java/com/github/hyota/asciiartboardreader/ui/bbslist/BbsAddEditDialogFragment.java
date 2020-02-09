package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.DialogBbsAddBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.value.LoadingStateValue;
import com.github.hyota.asciiartboardreader.ui.base.BaseDialogFragment;

import java.util.Arrays;
import java.util.Objects;

import timber.log.Timber;

public abstract class BbsAddEditDialogFragment extends BaseDialogFragment<BbsAddEditViewModel> {
    private static final String TAG = BbsAddEditDialogFragment.class.getSimpleName();

    private static final String ARG_BBS = "bbs";

    private DialogBbsAddBinding binding;
    @Nullable
    private Listener listener;

    public interface Listener {
        void onBbsAddEditComplete();
    }

    public static void show(@NonNull FragmentManager fragmentManager) {
        new BbsAddDialogFragment().show(fragmentManager, TAG);
    }

    public static void show(@NonNull FragmentManager fragmentManager, @NonNull Bbs bbs) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BBS, bbs);
        BbsAddEditDialogFragment dialogFragment = new BbsEditDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fragmentManager, TAG);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment fragment = getParentFragment();
        if (fragment != null) {
            if (fragment instanceof Listener) {
                listener = (Listener) fragment;
            } else {
                Timber.w("%s is not implemented BbsAddEditDialogFragment.Listener.", fragment);
            }
        } else {
            if (context instanceof Listener) {
                listener = (Listener) context;
            } else {
                Timber.w("%s is not implemented BbsAddEditDialogFragment.Listener.", context);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_bbs_add, null, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(getTitle())
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        alertDialog.setOnShowListener(this::onShow);
        setCancelable(true);
        return alertDialog;
    }

    @NonNull
    @Override
    protected Class<BbsAddEditViewModel> getViewModelClass() {
        return BbsAddEditViewModel.class;
    }

    protected void onShow(DialogInterface dialog) {
        Timber.d("onShow");
        if (!(dialog instanceof AlertDialog)) {
            Timber.w("%s is not AlertDialog.", dialog);
            return;
        }
        AlertDialog alertDialog = (AlertDialog) dialog;
        Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive.setOnClickListener(v -> onOkClick());
        Button negative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setOnClickListener(v -> dismiss());
        viewModel.getTitleError().observe(this, errorMessageModel -> {
            String error = null;
            if (errorMessageModel != null) {
                error = getString(errorMessageModel.getResId(), errorMessageModel.getParams());
            }
            binding.inputTitleLayout.setError(error);
        });
        viewModel.getUrlError().observe(this, errorMessageModel -> {
            String error = null;
            if (errorMessageModel != null) {
                error = getString(errorMessageModel.getResId(), errorMessageModel.getParams());
            }
            binding.inputUrlLayout.setError(error);
        });
        viewModel.getCanSubmit().observe(this, positive::setEnabled);
        viewModel.getLoadBbsTitleButtonState().observe(this, state -> {
            if (state == LoadingStateValue.LOADING) {
                binding.loadTitleButton.onStartLoading();
            } else {
                binding.loadTitleButton.onStopLoading();
            }
        });
        viewModel.getSubmitState().observe(this, state -> {
            if (state == LoadingStateValue.SUCCESS) {
                if (listener != null) {
                    listener.onBbsAddEditComplete();
                }
                dismiss();
            }
            if (state == LoadingStateValue.LOADING) {
                negative.setEnabled(false);
                setCancelable(false);
            } else {
                negative.setEnabled(true);
                setCancelable(true);
            }
        });
        binding.loadTitleButton.setButtonOnClickListener(v -> {
            if (binding.loadTitleButton.isEnabled()) {
                viewModel.loadTitle();
            }
        });
    }

    @StringRes
    protected abstract int getTitle();

    protected abstract void onOkClick();

    public static class BbsAddDialogFragment extends BbsAddEditDialogFragment {

        @Override
        protected int getTitle() {
            return R.string.bbs_add_title;
        }

        @Override
        protected void onOkClick() {
            viewModel.insert();
        }

        // TODO デバッグ用
        @Override
        protected void onShow(DialogInterface dialog) {
            super.onShow(dialog);
            if (com.github.hyota.asciiartboardreader.BuildConfig.DEBUG) {
                viewModel.setInitialValue(new Bbs("", "http", "yarufox.sakura.ne.jp", Arrays.asList("FOX")));
            }
        }

    }

    public static class BbsEditDialogFragment extends BbsAddEditDialogFragment {

        @Override
        protected void onShow(DialogInterface dialog) {
            super.onShow(dialog);
            Bundle args = Objects.requireNonNull(getArguments());
            Bbs initialValue = (Bbs) Objects.requireNonNull(args.getSerializable(ARG_BBS));
            viewModel.setInitialValue(initialValue);
        }

        @Override
        protected int getTitle() {
            return R.string.bbs_edit_title;
        }

        @Override
        protected void onOkClick() {
            viewModel.update();
        }
    }


}
