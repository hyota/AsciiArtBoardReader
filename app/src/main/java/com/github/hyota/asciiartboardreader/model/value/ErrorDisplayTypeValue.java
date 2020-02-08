package com.github.hyota.asciiartboardreader.model.value;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ErrorDisplayTypeValue {

    @IntDef({DIALOG, TOAST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorDisplayType {
    }

    public static final int DIALOG = 0;
    public static final int TOAST = 1;

}
