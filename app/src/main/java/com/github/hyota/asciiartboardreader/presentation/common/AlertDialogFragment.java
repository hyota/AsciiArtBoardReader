package com.github.hyota.asciiartboardreader.presentation.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.github.hyota.asciiartboardreader.domain.value.AlertDialogRequestCode;

import java.util.Objects;

public final class AlertDialogFragment extends DialogFragment {

    /**
     * MyDialog で何か処理が起こった場合にコールバックされるリスナ.
     */
    public interface Listener {

        /**
         * AlertDialogFragment で positiveButton, NegativeButton, リスト選択など行われた際に呼ばれる.
         *
         * @param requestCode AlertDialogFragment 実行時 requestCode
         * @param resultCode  DialogInterface.BUTTON_(POSI|NEGA)TIVE 若しくはリストの position
         * @param params      AlertDialogFragment に受渡した引数
         */
        void onAlertDialogSucceeded(@Nullable AlertDialogRequestCode requestCode, int resultCode,
                                    Bundle params);

        /**
         * AlertDialogFragment がキャンセルされた時に呼ばれる.
         *
         * @param requestCode AlertDialogFragment 実行時 requestCode
         * @param params      AlertDialogFragment に受渡した引数
         */
        void onAlertDialogCancelled(@Nullable AlertDialogRequestCode requestCode, Bundle params);
    }

    /**
     * MyDialogFragment を Builder パターンで生成する為のクラス.
     */
    public static class Builder {

        /** Context */
        @NonNull
        final Context context;
        /** FragmentManage. */
        @NonNull
        final FragmentManager fm;

        /** タイトル. */
        String title;

        /** メッセージ. */
        String message;

        /** 選択リスト. */
        String[] items;

        /** 肯定ボタン. */
        String positiveLabel;

        /** 否定ボタン. */
        String negativeLabel;

        /** リクエストコード. 親 Fragment 側の戻りで受け取る. */
        AlertDialogRequestCode requestCode = null;

        /** リスナに受け渡す任意のパラメータ. */
        Bundle params;

        /** DialogFragment のタグ. */
        String tag = "default";

        /** Dialog をキャンセル可かどうか. */
        boolean cancelable = true;

        /**
         * コンストラクタ.
         *
         * @param context Context
         * @param fm      FragmentManager
         */
        public Builder(@NonNull Context context, @NonNull FragmentManager fm) {
            this.fm = fm;
            this.context = context;
        }

        /**
         * タイトルを設定する.
         *
         * @param title タイトル
         * @return Builder
         */
        public Builder title(@NonNull final String title) {
            this.title = title;
            return this;
        }

        /**
         * タイトルを設定する.
         *
         * @param title タイトル
         * @return Builder
         */
        public Builder title(@StringRes final int title) {
            return title(context.getString(title));
        }

        /**
         * メッセージを設定する.
         *
         * @param message メッセージ
         * @return Builder
         */
        public Builder message(@NonNull final String message) {
            this.message = message;
            return this;
        }

        /**
         * メッセージを設定する.
         *
         * @param message メッセージ
         * @return Builder
         */
        public Builder message(@StringRes final int message) {
            return message(context.getString(message));
        }

        /**
         * 選択リストを設定する.
         *
         * @param items 選択リスト
         * @return Builder
         */
        public Builder items(@NonNull final String... items) {
            this.items = items;
            return this;
        }

        /**
         * 肯定ボタンを設定する.
         *
         * @param positiveLabel 肯定ボタンのラベル
         * @return Builder
         */
        public Builder positive(@NonNull final String positiveLabel) {
            this.positiveLabel = positiveLabel;
            return this;
        }

        /**
         * 肯定ボタンを設定する.
         *
         * @param positiveLabel 肯定ボタンのラベル
         * @return Builder
         */
        public Builder positive(@StringRes final int positiveLabel) {
            return positive(context.getString(positiveLabel));
        }

        /**
         * 否定ボタンを設定する.
         *
         * @param negativeLabel 否定ボタンのラベル
         * @return Builder
         */
        public Builder negative(@NonNull final String negativeLabel) {
            this.negativeLabel = negativeLabel;
            return this;
        }

        /**
         * 否定ボタンを設定する.
         *
         * @param negativeLabel 否定ボタンのラベル
         * @return Builder
         */
        public Builder negative(@StringRes final int negativeLabel) {
            return negative(context.getString(negativeLabel));
        }

        /**
         * リクエストコードを設定する.
         *
         * @param requestCode リクエストコード
         * @return Builder
         */
        public Builder requestCode(final AlertDialogRequestCode requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        /**
         * DialogFragment のタグを設定する.
         *
         * @param tag タグ
         * @return Builder
         */
        public Builder tag(final String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * Positive / Negative 押下時のリスナに受け渡すパラメータを設定する.
         *
         * @param params リスナに受け渡すパラメータ
         * @return Builder
         */
        public Builder params(final Bundle params) {
            this.params = new Bundle(params);
            return this;
        }

        /**
         * Dialog をキャンセルできるか否かをセットする.
         *
         * @param cancelable キャンセル可か否か
         * @return Builder
         */
        public Builder cancelable(final boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * DialogFragment を Builder に設定した情報を元に show する.
         */
        public void show() {
            final Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            args.putStringArray("items", items);
            args.putString("positive_label", positiveLabel);
            args.putString("negative_label", negativeLabel);
            args.putBoolean("cancelable", cancelable);
            if (params != null) {
                args.putBundle("params", params);
            }

            final AlertDialogFragment f = new AlertDialogFragment();
            args.putSerializable("request_code", requestCode);

            f.setArguments(args);
            f.show(fm, tag);
        }

    }

    /** Listener. */
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Object callback = getParentFragment();
        if (callback == null) {
            callback = getActivity();
        }
        if (!(callback instanceof Listener)) {
            throw new IllegalStateException("no implemented callback interface.");
        }
        listener = (Listener) callback;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = Objects.requireNonNull(getArguments());
        final DialogInterface.OnClickListener listener = (dialog, which) -> {
            dismiss();
            this.listener.onAlertDialogSucceeded(
                    (AlertDialogRequestCode) args.getSerializable("request_code"), which,
                    args.getBundle("params"));
        };
        final String title = args.getString("title");
        final String message = args.getString("message");
        final String[] items = args.getStringArray("items");
        final String positiveLabel = args.getString("positive_label");
        final String negativeLabel = args.getString("negative_label");
        setCancelable(args.getBoolean("cancelable"));
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (items != null && items.length > 0) {
            builder.setItems(items, listener);
        }
        if (!TextUtils.isEmpty(positiveLabel)) {
            builder.setPositiveButton(positiveLabel, listener);
        }
        if (!TextUtils.isEmpty(negativeLabel)) {
            builder.setNegativeButton(negativeLabel, listener);
        }
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Bundle args = Objects.requireNonNull(getArguments());
        listener.onAlertDialogCancelled(getRequestCode(), args.getBundle("params"));
    }

    /**
     * リクエストコードを取得する. Activity と ParentFragment 双方に対応するため.
     *
     * @return requestCode
     */
    @Nullable
    private AlertDialogRequestCode getRequestCode() {
        Bundle args = Objects.requireNonNull(getArguments());
        return args.containsKey("request_code")
                ? (AlertDialogRequestCode) args.getSerializable("request_code")
                : AlertDialogRequestCode.ordinalOf(getTargetRequestCode());
    }
}