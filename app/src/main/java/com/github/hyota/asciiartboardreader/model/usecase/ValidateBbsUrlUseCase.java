package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.model.entity.Setting;
import com.github.hyota.asciiartboardreader.model.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.model.repository.ShitarabaSettingRepository;
import com.github.hyota.asciiartboardreader.model.utils.ShitarabaUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

@Slf4j
public class ValidateBbsUrlUseCase {

    @Nonnull
    private SettingRepository settingRepository;
    @Nonnull
    private ShitarabaSettingRepository shitarabaSettingRepository;
    @Nonnull
    private ShitarabaUtils shitarabaUtils;

    @Inject
    public ValidateBbsUrlUseCase(@Nonnull SettingRepository settingRepository, @Nonnull ShitarabaSettingRepository shitarabaSettingRepository, @Nonnull ShitarabaUtils shitarabaUtils) {
        this.settingRepository = settingRepository;
        this.shitarabaSettingRepository = shitarabaSettingRepository;
        this.shitarabaUtils = shitarabaUtils;
    }

    public interface Callback {

        void onSuccess(@Nonnull String validatedUrl, @Nonnull Setting setting);

        void onInvalidUrl();

        void onFail();

    }

    public void execute(@Nonnull String url, @Nonnull Callback callback) {
        new Thread(() -> {
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl == null) {
                log.debug("url parse failed. url = {}", url);
                callback.onInvalidUrl();
                return;
            }
            if (shitarabaUtils.isShitarabaHost(httpUrl.host())) {
                execute(httpUrl, shitarabaSettingRepository, callback);
            } else {
                execute(httpUrl, settingRepository, callback);
            }
        }).start();
    }

    private void execute(@Nonnull HttpUrl url, @Nonnull SettingRepository settingRepository, @Nonnull Callback callback) {
        settingRepository.load(url, new SettingRepository.Callback() {
            @Override
            public void onSuccess(@Nonnull String validatedUrl, @Nullable Setting setting) {
                if (setting != null) {
                    callback.onSuccess(validatedUrl, setting);
                } else {
                    callback.onInvalidUrl();
                }
            }

            @Override
            public void onInvalidUrl() {
                if (url.pathSegments().isEmpty()) {
                    callback.onInvalidUrl();
                } else {
                    HttpUrl newUrl = url.newBuilder()
                            .removePathSegment(url.pathSize() - 1)
                            .build();
                    execute(newUrl, settingRepository, callback);
                }
            }

            @Override
            public void onFail() {
                callback.onFail();
            }
        });
    }

    private void execute(@Nonnull HttpUrl url, @Nonnull ShitarabaSettingRepository settingRepository, @Nonnull Callback callback) {
        settingRepository.load(url, new SettingRepository.Callback() {
            @Override
            public void onSuccess(@Nonnull String validatedUrl, @Nullable Setting setting) {
                if (setting != null) {
                    callback.onSuccess(validatedUrl, setting);
                } else {
                    callback.onInvalidUrl();
                }
            }

            @Override
            public void onInvalidUrl() {
                callback.onInvalidUrl();
            }

            @Override
            public void onFail() {
                callback.onFail();
            }
        });
    }

}
