package com.github.hyota.asciiartboardreader.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
public class Bbs {

    public static final long INITIAL_ID = -1;

    @PrimaryKey(autoGenerate = true)
    private long id = INITIAL_ID;
    @NonNull
    private String name;
    @NonNull
    private String url;

    public Bbs(@NonNull String name, @NonNull String url) {
        this.name = name;
        this.url = url;
    }
}
