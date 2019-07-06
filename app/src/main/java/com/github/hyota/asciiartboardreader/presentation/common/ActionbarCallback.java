package com.github.hyota.asciiartboardreader.presentation.common;

import androidx.annotation.NonNull;

/**
 * FragmentからToolbarへアクセスするためのコールバック.
 */
public interface ActionbarCallback {

    /**
     * タイトルを設定する.
     *
     * @param title タイトル
     */
    void setTitle(@NonNull String title);

}
