package com.github.hyota.asciiartboardreader.model.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Value;

@Value
public class Subject implements Serializable {

    @Nonnull
    List<BbsThread> bbsThreadList;

    public Subject(@Nonnull List<BbsThread> bbsThreadList) {
        this.bbsThreadList = Collections.unmodifiableList(bbsThreadList);
    }

}
