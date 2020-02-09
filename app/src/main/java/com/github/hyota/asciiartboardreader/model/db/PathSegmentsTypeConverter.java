package com.github.hyota.asciiartboardreader.model.db;

import androidx.room.TypeConverter;

import com.github.hyota.asciiartboardreader.model.entity.PathSegments;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathSegmentsTypeConverter {

    private Gson gson = new Gson();

    @TypeConverter
    public String fromStringList(PathSegments pathSegments) {
        if (pathSegments == null) {
            return null;
        }
        try {
            return gson.toJson(pathSegments);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @TypeConverter
    public PathSegments toStringList(String strings) {
        if (strings == null) {
            return null;
        }
        return gson.fromJson(strings, PathSegments.class);
    }
}
