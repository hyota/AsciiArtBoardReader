package com.github.hyota.asciiartboardreader.model.usecase;

import androidx.annotation.NonNull;

public interface Callback<T> {

    void apply(@NonNull T result);

}
