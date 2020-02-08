package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.model.entity.Setting;
import com.github.hyota.asciiartboardreader.model.net.SettingConverterFactory;

import org.jetbrains.annotations.NotNull;

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
public class LoadBbsTitleUseCase {

    @Nonnull
    private OkHttpClient client;
    @Nonnull
    private Converter<ResponseBody, Setting> converter;

    public interface Callback {

        void onSuccess(@Nonnull String validateUrl, @Nonnull String loadedTitle);

        void onInvalidUrl();

        void onFail();

    }

    @Inject
    public LoadBbsTitleUseCase(@Nonnull OkHttpClient client, @Nonnull SettingConverterFactory.SettingResponseConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    public void execute(@Nonnull String url, @Nonnull Callback callback) {
        new Thread(() -> {
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl == null) {
                callback.onInvalidUrl();
                return;
            }
            execute(httpUrl, callback);
        }).start();
    }

    private void execute(@Nonnull HttpUrl url, @Nonnull Callback callback) {
        HttpUrl settingUrl = url.newBuilder()
                .addPathSegment("SETTING.TXT")
                .build();
        log.debug("get SETTING.TXT from {}", settingUrl.toString());
        Request request = new Request.Builder()
                .url(settingUrl)
                .get()
                .build();
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
                        Setting setting = converter.convert(Objects.requireNonNull(response.body()));
                        callback.onSuccess(url.toString(), Objects.requireNonNull(setting).getBbsTitle());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        callback.onFail();
                    }
                } else {
                    if (url.pathSegments().isEmpty()) {
                        callback.onInvalidUrl();
                    } else {
                        HttpUrl newUrl = url.newBuilder()
                                .removePathSegment(url.pathSize() - 1)
                                .build();
                        execute(newUrl, callback);
                    }
                }
            }
        });
    }

}
