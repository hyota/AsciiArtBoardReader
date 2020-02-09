package com.github.hyota.asciiartboardreader.model.utils;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.ShitarabaBbs;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

@Slf4j
public class ShitarabaUtils {

    private static final Set<String> SHITARABA_HOST_SET = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "jbbs.shitaraba.com",
                    "jbbs.livedoor.com",
                    "jbbs.livedoor.jp",
                    "jbbs.shitaraba.net"
            )));
    private static final Pattern SHITARABA_PATTERN = Pattern.compile(
            "^https?://(?:(?:jbbs\\.shitaraba\\.com)|(?:jbbs\\.livedoor\\.com)|(?:jbbs\\.livedoor\\.jp)|(?:jbbs\\.shitaraba\\.net))/(?:bbs/read\\.cgi/)?([^/]+)/(\\d+).*$");

    @Inject
    public ShitarabaUtils() {
    }

    public boolean isShitarabaHost(@Nullable String host) {
        return SHITARABA_HOST_SET.contains(host);
    }

    @Nullable
    public Bbs convert(@Nullable Bbs bbs) {
        if (bbs == null) {
            return null;
        }
        if (!isShitarabaHost(bbs.getHost())) {
            return bbs;
        }
        Matcher m = SHITARABA_PATTERN.matcher(bbs.getHttpUrl().toString());
        if (!m.matches()) {
            return bbs;
        }
        try {
            return new ShitarabaBbs(bbs, m.group(1), Long.parseLong(m.group(2)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return bbs;
        }
    }

    @Nullable
    public ShitarabaBbs toShitarabaBbs(@Nonnull HttpUrl httpUrl) {
        return toShitarabaBbs("", httpUrl);
    }

    @Nullable
    public ShitarabaBbs toShitarabaBbs(@Nonnull String title, @Nonnull HttpUrl httpUrl) {
        if (!isShitarabaHost(httpUrl.host())) {
            log.debug("host is not shitaraba host. host = {}", httpUrl.host());
            return null;
        }
        Matcher m = SHITARABA_PATTERN.matcher(httpUrl.toString());
        if (!m.matches()) {
            log.debug("url is not shitaraba url pattern. url = {}", httpUrl);
            return null;
        }
        try {
            return new ShitarabaBbs(title, httpUrl.scheme(), httpUrl.host(), httpUrl.pathSegments(), m.group(1), Long.parseLong(m.group(2)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

}
