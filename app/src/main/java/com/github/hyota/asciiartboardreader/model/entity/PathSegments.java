package com.github.hyota.asciiartboardreader.model.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Value;

@Value
public class PathSegments implements Serializable {

    List<String> values;

    public PathSegments(List<String> values) {
        this.values = Collections.unmodifiableList(values);
    }
}
