package com.github.hyota.asciiartboardreader.domain.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

import lombok.Getter;

/**
 * 板情報.
 */
public class Board implements Serializable {

    /**
     * コンストラクタ.
     *
     * @param id    ID
     * @param url   URL
     * @param title タイトル
     */
    public Board(int id, @NonNull String url, @NonNull String title) {
        this.id = id;
        this.url = url;
        this.title = title;
    }

    /** id. */
    @Getter
    private int id;

    /** URL. */
    @Getter
    @NonNull
    private String url;

    /** タイトル. */
    @Getter
    @NonNull
    private String title;

}
