package com.github.hyota.asciiartboardreader.presentation.boardlist;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.entity.Board;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;


public class BoardCreateEditDialogFragment extends DialogFragment implements BoardCreateEditContract.View {
    private static final String TAG = BoardCreateEditDialogFragment.class.getSimpleName();
    private static final String ARG_EDIT_TARGET = BoardCreateEditDialogFragment.class.getSimpleName() + ".editTarget";

    @Inject
    BoardCreateEditContract.Presenter presenter;
    @BindView(R.id.url)
    EditText url;
    @BindView(R.id.title)
    EditText title;

    private Context context;
    private Listener listener;
    private Board target;

    public interface Listener {

        void onSucceeded();

        void onCanceled();

    }

    public static void show(@NonNull FragmentManager manager) {
        new BoardCreateEditDialogFragment().show(manager, TAG);
    }

    public static void show(@NonNull FragmentManager manager, @NonNull Board board) {
        BoardCreateEditDialogFragment fragment = new BoardCreateEditDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EDIT_TARGET, board);
        fragment.setArguments(args);
        fragment.show(manager, TAG);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.context = context;
        Object listener = getParentFragment();
        if (listener == null) {
            listener = context;
        }
        if (listener instanceof Listener) {
            this.listener = (Listener) listener;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.dialog_board_create_edit, null);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            target = (Board) args.getSerializable(ARG_EDIT_TARGET);
        }
        if (target != null) {
            url.setText(target.getUrl());
            title.setText(target.getTitle());
        } else {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData cd = cm.getPrimaryClip();
            if (cd != null) {
                ClipData.Item item = cd.getItemAt(0);
                url.setText(item.getText());
            }
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(target == null ? R.string.title_board_create : R.string.title_board_edit)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (target == null) {
                        presenter.save(url.getText().toString(), title.getText().toString());
                    } else {
                        presenter.save(target.getId(), url.getText().toString(), title.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                })
                .setNeutralButton(R.string.button_get, (dialog, which) ->
                        presenter.loadTitleFromServer(url.getText().toString()))
                .create();
        alertDialog.setOnShowListener(dialog -> {
            url.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                            .setEnabled(!url.getText().toString().isEmpty() && !title.getText().toString().isEmpty());
                    ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL)
                            .setEnabled(!url.getText().toString().isEmpty());
                }
            });
            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                            .setEnabled(!url.getText().toString().isEmpty() && !title.getText().toString().isEmpty());
                }
            });
        });
        return alertDialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        listener = null;
    }

    @Override
    public void setBoardTitle(@Nonnull String title) {
        this.title.setText(title);
    }

    @Override
    public void duplicatedError() {
        Toast.makeText(context, R.string.error_duplication, Toast.LENGTH_LONG).show();
    }

    @Override
    public void invalidateError() {
        Toast.makeText(context, R.string.error_invalidation, Toast.LENGTH_LONG).show();
    }

}
