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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.DialogBbsAddBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public abstract class BbsAddEditDialogFragment extends DialogFragment {
    private static final String TAG = BbsAddEditDialogFragment.class.getSimpleName();

    private static final String ARG_BBS = "bbs";

    @Inject
    BbsAddEditViewModel viewModel;
    private DialogBbsAddBinding binding;
    protected Context context;

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
        AndroidSupportInjection.inject(this);
        this.context = context;
        super.onAttach(context);
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
        alertDialog.setOnShowListener(dialog -> {
            Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> onOkClick());
            viewModel.getCanSubmit().observe(this, positive::setEnabled);
            setInitializeValue();
        });
        setCancelable(true);
        return alertDialog;
    }

    @StringRes
    protected abstract int getTitle();

    protected abstract void setInitializeValue();

    protected abstract void onOkClick();

    public static class BbsAddDialogFragment extends BbsAddEditDialogFragment {

        @Override
        protected int getTitle() {
            return R.string.bbs_add_title;
        }

        @Override
        protected void setInitializeValue() {
            // NOOP
        }

        @Override
        protected void onOkClick() {
            viewModel.create();
        }
    }

    public static class BbsEditDialogFragment extends BbsAddEditDialogFragment {

        @Override
        protected int getTitle() {
            return R.string.bbs_edit_title;
        }

        @Override
        protected void setInitializeValue() {
            Bundle args = Objects.requireNonNull(getArguments());
            Bbs initialValue = (Bbs) Objects.requireNonNull(args.getSerializable(ARG_BBS));
            viewModel.setInitialValue(initialValue);
        }

        @Override
        protected void onOkClick() {
            viewModel.update();
        }
    }


}
