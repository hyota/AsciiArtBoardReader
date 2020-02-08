package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.DialogBbsAddBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.value.LoadingStateValue;
import com.github.hyota.asciiartboardreader.ui.common.BaseDialogFragment;

import java.util.Objects;

import timber.log.Timber;

public abstract class BbsAddEditDialogFragment extends BaseDialogFragment<BbsAddEditViewModel> {
    private static final String TAG = BbsAddEditDialogFragment.class.getSimpleName();

    private static final String ARG_BBS = "bbs";

    private DialogBbsAddBinding binding;

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
                .setNeutralButton(android.R.string.cancel, (dialog, which) -> {
                    // NOOP
                })
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
        viewModel.getTitleError().observe(this, binding.inputTitleLayout::setError);
        viewModel.getUrlError().observe(this, binding.inputUrlLayout::setError);
        viewModel.getCanSubmit().observe(this, positive::setEnabled);
        viewModel.getLoadBbsTitleButtonState().observe(this, state -> {
            if (state == LoadingStateValue.LOADING) {
                binding.inputUrl.setEnabled(false);
                binding.inputTitle.setEnabled(false);
                binding.loadTitleButton.onStartLoading();
            } else {
                binding.inputUrl.setEnabled(true);
                binding.inputTitle.setEnabled(true);
                binding.loadTitleButton.onStopLoading();
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
            viewModel.create();
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
