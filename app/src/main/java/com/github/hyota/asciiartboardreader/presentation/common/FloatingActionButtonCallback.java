package com.github.hyota.asciiartboardreader.presentation.common;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

/**
 * Fragmentからフローティングアクションボタンへアクセスするためのコールバック.
 */
public interface FloatingActionButtonCallback {

    /**
     * リスナーをセットする.
     *
     * @param listener リスナー
     */
    void setOnClickListener(@Nullable View.OnClickListener listener);

    /**
     * 有効性を変更する.
     *
     * @param enabled 有効性
     */
    void setEnabled(boolean enabled);

    /**
     * イメージを設定する.
     *
     * @param resId イメージリソースID
     */
    void setImage(@DrawableRes int resId);

}
