package com.github.hyota.asciiartboardreader.presentation.common;

import androidx.annotation.NonNull;

/**
 * FragmentからToolbarへ悪世するためのコールバック.
 */
public interface ToolbarCallback {

    /**
     * タイトルを設定する.
     *
     * @param title タイトル
     */
    void setTitle(@NonNull String title);

}
