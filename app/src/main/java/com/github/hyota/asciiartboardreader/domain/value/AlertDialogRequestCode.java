package com.github.hyota.asciiartboardreader.domain.value;

import com.annimon.stream.Stream;

import javax.annotation.Nullable;

public enum AlertDialogRequestCode {

    BOARD_DELETE,
    ;

    @Nullable
    public static AlertDialogRequestCode ordinalOf(int ordinal) {
        return Stream.of(values()).filter(it -> it.ordinal() == ordinal).findFirst().orElse(null);
    }
}
