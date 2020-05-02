package com.github.hyota.asciiartboardreader.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import timber.log.Timber;

@Entity(indices = {
        @Index(value = {"title"}, unique = true),
        @Index(value = {"scheme", "host", "path"}, unique = true)
})
@Data
@Slf4j
public class Bbs implements Serializable {

    public static final long INITIAL_ID = 0;
    private static final Charset DEFAULT_CHARSET = Charset.forName("Shift_JIS");

    @PrimaryKey(autoGenerate = true)
    final protected long id;
    @NonNull
    final protected String title;
    @Nonnull
    final protected String scheme;
    @Nonnull
    final protected String host;
    @Nonnull
    final protected PathSegments path;
    @Nullable
    final protected String charsetString;

    public Bbs(long id, @NonNull String title, @Nonnull String scheme, @Nonnull String host, @Nonnull PathSegments path, @Nullable String charsetString) {
        this.id = id;
        this.title = title;
        this.scheme = scheme;
        this.host = host;
        this.path = path;
        this.charsetString = charsetString;
    }

    @Ignore
    public Bbs(long id, @NonNull String title, @Nonnull String scheme, @Nonnull String host, @Nonnull List<String> path, @Nullable String charsetString) {
        this.id = id;
        this.title = title;
        this.scheme = scheme;
        this.host = host;
        this.path = new PathSegments(Stream.of(path).filter(it -> it != null && !it.isEmpty()).collect(Collectors.toList()));
        this.charsetString = charsetString;
    }

    @Ignore
    public Bbs(@NonNull String title, @Nonnull String scheme, @Nonnull String host, @Nonnull List<String> path) {
        this(INITIAL_ID, title, scheme, host, path, null);
    }

    @Ignore
    public Bbs(@Nonnull String title, @Nonnull HttpUrl httpUrl) {
        this(INITIAL_ID, title, httpUrl.scheme(), httpUrl.host(), httpUrl.pathSegments(), null);
    }

    @Ignore
    public Bbs(long id, @Nonnull String title, @Nullable String charsetString, @Nonnull HttpUrl httpUrl) {
        this(id, title, httpUrl.scheme(), httpUrl.host(), httpUrl.pathSegments(), charsetString);
    }

    @Nonnull
    public Charset getCharset() {
        log.debug("title:{}, charset:{}", title, charsetString);
        if (charsetString != null) {
            try {
                return Charset.forName(charsetString);
            } catch (Exception e) {
                log.error("{} is not charset.", charsetString);
            }
        }
        return getDefaultCharset();
    }

    @Nonnull
    protected Charset getDefaultCharset() {
        return DEFAULT_CHARSET;
    }

    @Nonnull
    public HttpUrl getHttpUrl() {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(scheme)
                .host(host);
        Stream.of(path.getValues()).forEach(builder::addPathSegment);
        return builder.build();
    }

    @Nonnull
    public String toUrlString() {
        return getHttpUrl().toString();
    }

    @Nonnull
    public String toEncodeUrlString() {
        try {
            return URLEncoder.encode(toUrlString(), "UTF-8")
                    .replace("*", "%2a")
                    .replace("-", "%2d")
                    .replace("+", "%20")
                    .toUpperCase();
        } catch (UnsupportedEncodingException e) {
            Timber.e(e);
            throw new RuntimeException(e);
        }
    }

}
