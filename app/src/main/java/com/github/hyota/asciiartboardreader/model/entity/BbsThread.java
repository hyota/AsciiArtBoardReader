package com.github.hyota.asciiartboardreader.model.entity;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.Serializable;

import javax.annotation.Nonnull;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class BbsThread implements Serializable {

    private static final DateTimeFormatter MM_DD_HH_MM_E = DateTimeFormatter.ofPattern("MM/dd HH:mm(E)");
    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_E = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm(E)");

    long id;
    long unixTime;
    @Nonnull
    String title;
    long count;

    public double getMomentum() {
        return 1.0 * count / (LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() - unixTime);
    }

    public LocalDateTime getSince() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
    }

    public String getSinceString() {
        if (LocalDateTime.now().getYear() == getSince().getYear()) {
            return MM_DD_HH_MM_E.format(getSince());
        }
        return YYYY_MM_DD_HH_MM_E.format(getSince());
    }
}
