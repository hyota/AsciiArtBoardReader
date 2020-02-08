package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.DialogBbsAddBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.value.LoadingStateValue;
import com.github.hyota.asciiartboardreader.ui.common.BaseDialogFragment;

import java.util.Objects;

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
        alertDialog.setOnShowListener(dialog -> {
            Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> onOkClick());
            viewModel.getCanSubmit().observe(this, positive::setEnabled);
            setInitializeValue();
        });
        setCancelable(true);
        return alertDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getLoadBbsTitleButtonState().observe(getViewLifecycleOwner(), state -> {
            if (state == LoadingStateValue.LOADING) {
                binding.getTitleButton.startAnimation();
            } else if (state == LoadingStateValue.SUCCESS) {
                binding.getTitleButton.doneLoadingAnimation(ContextCompat.getColor(context, R.color.colorPrimary),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_done_white_48dp));
            } else {
                binding.getTitleButton.revertAnimation();
            }
        });
        binding.getTitleButton.setOnClickListener(v -> viewModel.loadBbsTitle());
    }

    @NonNull
    @Override
    protected Class<BbsAddEditViewModel> getViewModelClass() {
        return BbsAddEditViewModel.class;
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
