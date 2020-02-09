package com.github.hyota.asciiartboardreader.model.entity;

import androidx.annotation.NonNull;

import java.util.List;

import javax.annotation.Nonnull;

import lombok.Getter;

public class ShitarabaBbs extends Bbs {

    private static final String SHITARABA_HOST = "jbbs.shitaraba.net";

    @Getter
    @Nonnull
    private final String category;
    @Getter
    private final long address;

    public ShitarabaBbs(@NonNull String title, @Nonnull String scheme, @Nonnull String host, @Nonnull List<String> path, @Nonnull String category, long address) {
        super(title, scheme, host, path);
        this.category = category;
        this.address = address;
    }

    public ShitarabaBbs(@Nonnull Bbs bbs, @Nonnull String category, long address) {
        super(bbs.getId(), bbs.getTitle(), bbs.getScheme(), SHITARABA_HOST, bbs.getPath());
        this.category = category;
        this.address = address;
    }

}
