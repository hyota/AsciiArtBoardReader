package com.github.hyota.asciiartboardreader.model.repository;

import com.github.hyota.asciiartboardreader.di.annotation.Local;
import com.github.hyota.asciiartboardreader.model.common.InputStreamWithOutput;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.net.converter.SubjectConverterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;

@Slf4j
public class SubjectRemoteRepositoryImpl implements SubjectRepository {

    @Nonnull
    private OkHttpClient client;
    @Nonnull
    private File storage;

    @Inject
    public SubjectRemoteRepositoryImpl(@Nonnull OkHttpClient client, @Local @Nonnull File storage) {
        this.client = client;
        this.storage = storage;
    }

    @Override
    public void load(@Nonnull Bbs bbs, @Nonnull Callback callback) {
        HttpUrl url = bbs.getHttpUrl();
        HttpUrl subjectUrl = url.newBuilder()
                .addPathSegment("subject.txt")
                .build();
        log.debug("get subject.txt from {}", subjectUrl.toString());
        Request request = new Request.Builder()
                .url(subjectUrl)
                .get()
                .build();

        File subjectFile = getSubjectFile(storage, bbs);
        Converter<ResponseBody, Subject> converter = new SubjectConverterFactory.SubjectResponseConverter(bbs.getCharset(), is -> new InputStreamWithOutput(is, subjectFile));

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFail();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                log.debug("onResponse url = {}, status = {}", url.toString(), response.code());
                if (response.isSuccessful()) {
                    try {
                        Subject subject = converter.convert(Objects.requireNonNull(response.body()));
                        if (subject != null) {
                            callback.onSuccess(subject);
                        } else {
                            callback.onFail();
                        }
                    } catch (Exception e) {
                        callback.onFail();
                    }
                } else {
                    callback.onNotFound();
                }
            }
        });
    }
}
