package com.github.hyota.asciiartboardreader.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Data;
import okhttp3.HttpUrl;

@Entity(indices = {
        @Index(value = {"title"}, unique = true),
        @Index(value = {"scheme", "host", "path"}, unique = true)
})
@Data
public class Bbs implements Serializable {

    public static final long INITIAL_ID = 0;

    @PrimaryKey(autoGenerate = true)
    private final long id;
    @NonNull
    private final String title;
    @Nonnull
    private final String scheme;
    @Nonnull
    private final String host;
    @Nonnull
    private final PathSegments path;

    public Bbs(long id, @NonNull String title, @Nonnull String scheme, @Nonnull String host, @Nonnull PathSegments path) {
        this.id = id;
        this.title = title;
        this.scheme = scheme;
        this.host = host;
        this.path = path;
    }

    @Ignore
    public Bbs(long id, @NonNull String title, @Nonnull String scheme, @Nonnull String host, @Nonnull List<String> path) {
        this.id = id;
        this.title = title;
        this.scheme = scheme;
        this.host = host;
        this.path = new PathSegments(Stream.of(path).filter(it -> it != null && !it.isEmpty()).collect(Collectors.toList()));
    }

    @Ignore
    public Bbs(@NonNull String title, @Nonnull String scheme, @Nonnull String host, @Nonnull List<String> path) {
        this(INITIAL_ID, title, scheme, host, path);
    }

    @Ignore
    public Bbs(@Nonnull String title, @Nonnull HttpUrl httpUrl) {
        this(INITIAL_ID, title, httpUrl.scheme(), httpUrl.host(), httpUrl.pathSegments());
    }

    @Ignore
    public Bbs(long id, @Nonnull String title, @Nonnull HttpUrl httpUrl) {
        this(id, title, httpUrl.scheme(), httpUrl.host(), httpUrl.pathSegments());
    }

    public HttpUrl getHttpUrl() {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(scheme)
                .host(this.host);
        Stream.of(path.getValues()).forEach(builder::addPathSegment);
        return builder.build();
    }

}
