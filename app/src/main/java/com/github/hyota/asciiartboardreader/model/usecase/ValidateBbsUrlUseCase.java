package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.di.annotation.Remote;
import com.github.hyota.asciiartboardreader.di.annotation.Shitaraba;
import com.github.hyota.asciiartboardreader.model.entity.Setting;
import com.github.hyota.asciiartboardreader.model.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.model.utils.ShitarabaUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

@Slf4j
public class ValidateBbsUrlUseCase {

    @Nonnull
    private ExecutorService executorService;
    @Nonnull
    private SettingRepository settingRepository;
    @Nonnull
    private SettingRepository settingShitarabaRepository;
    @Nonnull
    private ShitarabaUtils shitarabaUtils;

    @Inject
    public ValidateBbsUrlUseCase(@Nonnull ExecutorService executorService, @Nonnull @Remote SettingRepository settingRepository, @Nonnull @Shitaraba SettingRepository settingShitarabaRepository, @Nonnull ShitarabaUtils shitarabaUtils) {
        this.executorService = executorService;
        this.settingRepository = settingRepository;
        this.settingShitarabaRepository = settingShitarabaRepository;
        this.shitarabaUtils = shitarabaUtils;
    }

    public interface Callback {

        void onSuccess(@Nonnull String validatedUrl, @Nonnull Setting setting);

        void onInvalidUrl();

        void onFail();

    }

    public Future<?> execute(@Nonnull String url, @Nonnull Callback callback) {
        return executorService.submit(() -> {
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl == null) {
                log.debug("url parse failed. url = {}", url);
                callback.onInvalidUrl();
                return;
            }
            execute(httpUrl, getSettingRepository(httpUrl.host()), callback);
        });
    }

    private void execute(@Nonnull HttpUrl httpUrl, @Nonnull SettingRepository settingRepository, @Nonnull Callback callback) {
        settingRepository.load(httpUrl, new SettingRepository.Callback() {
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
                if (shitarabaUtils.isShitarabaHost(httpUrl.host()) || httpUrl.pathSegments().isEmpty()) {
                    callback.onInvalidUrl();
                } else {
                    HttpUrl newUrl = httpUrl.newBuilder()
                            .removePathSegment(httpUrl.pathSize() - 1)
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

    @Nonnull
    private SettingRepository getSettingRepository(@Nonnull String host) {
        if (shitarabaUtils.isShitarabaHost(host)) {
            return settingShitarabaRepository;
        } else {
            return settingRepository;
        }
    }

}
