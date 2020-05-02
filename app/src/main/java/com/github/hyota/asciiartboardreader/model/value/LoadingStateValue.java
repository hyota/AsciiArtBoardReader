package com.github.hyota.asciiartboardreader.model.value;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LoadingStateValue {

    @IntDef({NONE, LOADING, SUCCESS, FAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadingState {
    }

    public static final int NONE = 0;
    public static final int LOADING = 1;
    public static final int SUCCESS = 2;
    public static final int FAIL = 3;

    public static boolean isLoading(@LoadingState Integer state) {
        return state == LOADING;
    }

}
