package com.github.hyota.asciiartboardreader.model.repository;

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
public class SettingRepositoryImpl implements SettingRepository {

    @Nonnull
    private OkHttpClient client;
    @Nonnull
    private Converter<ResponseBody, Setting> converter;

    @Inject
    public SettingRepositoryImpl(@Nonnull OkHttpClient client, @Nonnull SettingConverterFactory.SettingResponseConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    @Override
    public void load(@Nonnull HttpUrl url, @Nonnull Callback callback) {
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
                        callback.onSuccess(url.toString(), setting);
                    } catch (Exception e) {
                        callback.onSuccess(url.toString(), null);
                    }
                } else {
                    callback.onInvalidUrl();
                }
            }
        });
    }
}
