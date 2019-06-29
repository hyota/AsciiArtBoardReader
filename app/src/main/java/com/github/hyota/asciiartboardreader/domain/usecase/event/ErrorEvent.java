package com.github.hyota.asciiartboardreader.domain.usecase.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * EventBusでの例外通知イベント.
 */
@AllArgsConstructor
public class ErrorEvent {

    /**
     * 通知する例外.
     */
    @Getter
    final private Throwable throwable;

}
