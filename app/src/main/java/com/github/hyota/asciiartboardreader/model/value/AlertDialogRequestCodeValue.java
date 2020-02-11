package com.github.hyota.asciiartboardreader.model.value;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AlertDialogRequestCodeValue {

    @IntDef({BBS_DELETE_CONFIRM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlertDialogRequestCode {
    }

    public static final int NONE = 0;
    public static final int BBS_DELETE_CONFIRM = 1;

}
