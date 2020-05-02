package com.github.hyota.asciiartboardreader.model.db;

import androidx.room.TypeConverter;

import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharsetTypeConverter {

    @TypeConverter
    public String fromCharset(Charset charset) {
        if (charset == null) {
            return null;
        }
        return charset.name();
    }

    @TypeConverter
    public Charset toString(String string) {
        if (string != null && !string.isEmpty()) {
            try {
                return Charset.forName(string);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }
}
