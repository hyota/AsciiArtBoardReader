package com.github.hyota.asciiartboardreader.ui.base;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public interface HasFloatingActionButton {

    void showFloatingActionButton();

    void hideFloatingActionButton();

    void setFloatingActionButtonImageResource(@DrawableRes int resId);

    void setFloatingActionButtonOnClickListener(@Nullable View.OnClickListener listener);

}
