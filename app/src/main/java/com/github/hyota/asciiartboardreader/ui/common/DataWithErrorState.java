package com.github.hyota.asciiartboardreader.ui.common;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DataWithErrorState<T> {

    T data;

    boolean success;

    public DataWithErrorState(T data) {
        this.data = data;
        this.success = true;
    }

    public static <T> DataWithErrorState<T> error() {
        return new DataWithErrorState<>(null, false);
    }

}
