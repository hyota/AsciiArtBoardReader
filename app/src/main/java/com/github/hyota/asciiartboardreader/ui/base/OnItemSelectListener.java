package com.github.hyota.asciiartboardreader.ui.base;

import androidx.annotation.NonNull;

@FunctionalInterface
public interface OnItemSelectListener<T> {

    void onItemSelect(@NonNull T item);

}
