package com.github.hyota.asciiartboardreader.ui.common;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.github.hyota.asciiartboardreader.model.value.ErrorDisplayTypeValue;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorMessageModel {

    @StringRes
    int resId;

    @Nullable
    Object[] params;

    @ErrorDisplayTypeValue.ErrorDisplayType
    int displayType;

    public ErrorMessageModel(int resId, @ErrorDisplayTypeValue.ErrorDisplayType int displayType) {
        this.resId = resId;
        params = null;
        this.displayType = displayType;
    }
}
