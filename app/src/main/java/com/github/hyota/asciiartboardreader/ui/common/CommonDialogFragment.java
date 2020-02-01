package com.github.hyota.asciiartboardreader.ui.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

public class CommonDialogFragment extends DialogFragment {

    /**
     * 何か処理が起こった場合にコールバックされるリスナ.
     */
    public interface Callback {

        /**
         * positiveButton, NegativeButton, リスト選択など行われた際に呼ばれる.
         *
         * @param requestCode MyDialogFragment 実行時 requestCode
         * @param resultCode  DialogInterface.BUTTON_(POSI|NEGA)TIVE 若しくはリストの position
         * @param params      MyDialogFragment に受渡した引数
         */
        void onCommonDialogSucceeded(int requestCode, int resultCode, Bundle params);

        /**
         * キャンセルされた時に呼ばれる.
         *
         * @param requestCode MyDialogFragment 実行時 requestCode
         * @param params      MyDialogFragment に受渡した引数
         */
        void onCommonDialogCancelled(int requestCode, Bundle params);
    }

    /**
     * MyDialogFragment を Builder パターンで生成する為のクラス.
     */
    public static class Builder {

        /** Activity. */
        private final AppCompatActivity mActivity;

        /** 親 Fragment. */
        private final Fragment mParentFragment;

        /** タイトル. */
        @Accessors(chain = true)
        @Setter
        private String title;

        /** メッセージ. */
        @Accessors(chain = true)
        @Setter
        private String message;

        /** 選択リスト. */
        @Accessors(chain = true)
        @Setter
        private String[] items;

        /** 肯定ボタン. */
        @Accessors(chain = true)
        @Setter
        private String positive;

        /** 否定ボタン. */
        @Accessors(chain = true)
        @Setter
        private String negative;

        /** リクエストコード. 親 Fragment 側の戻りで受け取る. */
        @Accessors(chain = true)
        @Setter
        int requestCode = -1;

        /** リスナに受け渡す任意のパラメータ. */
        @Accessors(chain = true)
        @Setter
        private Bundle params;

        /** DialogFragment のタグ. */
        @Accessors(chain = true)
        @Setter
        private String tag = "default";

        /** Dialog をキャンセル可かどうか. */
        @Accessors(chain = true)
        @Setter
        private boolean cancelable = true;

        @NonNull
        private Context context;

        /**
         * コンストラクタ. Activity 上から生成する場合.
         *
         * @param activity 親 Activity
         */
        public <A extends AppCompatActivity & Callback> Builder(@NonNull final A activity) {
            mActivity = activity;
            mParentFragment = null;
        }

        /**
         * コンストラクタ. Fragment 上から生成する場合.
         *
         * @param parentFragment 親 Fragment
         */
        public <F extends Fragment & Callback> Builder(@NonNull final F parentFragment) {
            mParentFragment = parentFragment;
            mActivity = null;
        }

        /**
         * タイトルを設定する.
         *
         * @param title タイトル
         * @return Builder
         */
        @Tolerate
        public Builder setTitle(@StringRes final int title) {
            return setTitle(getContext().getString(title));
        }

        /**
         * メッセージを設定する.
         *
         * @param message メッセージ
         * @return Builder
         */
        @Tolerate
        public Builder setMessage(@StringRes final int message) {
            return setMessage(getContext().getString(message));
        }

        /**
         * 肯定ボタンを設定する.
         *
         * @param positive 肯定ボタンのラベル
         * @return Builder
         */
        @Tolerate
        public Builder setPositive(@StringRes final int positive) {
            return setPositive(getContext().getString(positive));
        }

        /**
         * 否定ボタンを設定する.
         *
         * @param negative 否定ボタンのラベル
         * @return Builder
         */
        @Tolerate
        public Builder setNnegative(@StringRes final int negative) {
            return setNegative(getContext().getString(negative));
        }

        /**
         * DialogFragment を Builder に設定した情報を元に show する.
         */
        public void show() {
            final Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            args.putStringArray("items", items);
            args.putString("positive_label", positive);
            args.putString("negative_label", negative);
            args.putBoolean("cancelable", cancelable);
            if (params != null) {
                args.putBundle("params", params);
            }

            final CommonDialogFragment f = new CommonDialogFragment();
            if (mParentFragment != null) {
                f.setTargetFragment(mParentFragment, requestCode);
            } else {
                args.putInt("request_code", requestCode);
            }
            f.setArguments(args);
            if (mParentFragment != null) {
                f.show(mParentFragment.getChildFragmentManager(), tag);
            } else if (mActivity != null) {
                f.show(mActivity.getSupportFragmentManager(), tag);
            }
        }

        /**
         * コンテキストを取得する. getString() 呼び出しの為.
         *
         * @return Context
         */
        private Context getContext() {
            return (mActivity == null ? mParentFragment.getActivity() : mActivity).getApplicationContext();
        }
    }

    /** Callback. */
    private Callback mCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Object callback = getParentFragment();
        if (callback == null) {
            callback = getActivity();
            if (!(callback instanceof Callback)) {
                throw new IllegalStateException();
            }
        }
        mCallback = (Callback) callback;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final DialogInterface.OnClickListener listener = (dialog, which) -> {
            dismiss();
            mCallback.onCommonDialogSucceeded(getRequestCode(), which, getArguments().getBundle("params"));
        };
        final String title = getArguments().getString("title");
        final String message = getArguments().getString("message");
        final String[] items = getArguments().getStringArray("items");
        final String positiveLabel = getArguments().getString("positive_label");
        final String negativeLabel = getArguments().getString("negative_label");
        setCancelable(getArguments().getBoolean("cancelable"));
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
    public void onCancel(@NonNull DialogInterface dialog) {
        mCallback.onCommonDialogCancelled(getRequestCode(), getArguments().getBundle("params"));
    }

    /**
     * リクエストコードを取得する. Activity と ParentFragment 双方に対応するため.
     *
     * @return requestCode
     */
    private int getRequestCode() {
        return getArguments().containsKey("request_code") ? getArguments().getInt("request_code") : getTargetRequestCode();
    }
}
