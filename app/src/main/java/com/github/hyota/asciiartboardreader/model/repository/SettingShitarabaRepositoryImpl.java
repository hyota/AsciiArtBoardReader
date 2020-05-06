package com.github.hyota.asciiartboardreader.model.repository;

import com.github.hyota.asciiartboardreader.model.entity.Setting;
import com.github.hyota.asciiartboardreader.model.entity.ShitarabaBbs;
import com.github.hyota.asciiartboardreader.model.net.ShitarabaService;
import com.github.hyota.asciiartboardreader.model.utils.ShitarabaUtils;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

@Slf4j
public class SettingShitarabaRepositoryImpl implements SettingRepository {

    @Nonnull
    private Retrofit retrofit;
    @Nonnull
    private ShitarabaUtils shitarabaUtils;

    @Inject
    public SettingShitarabaRepositoryImpl(@Nonnull Retrofit retrofit, @Nonnull ShitarabaUtils shitarabaUtils) {
        this.retrofit = retrofit;
        this.shitarabaUtils = shitarabaUtils;
    }

    @Override
    public void load(@Nonnull HttpUrl url, @Nonnull Callback callback) {
        ShitarabaBbs bbs = shitarabaUtils.toShitarabaBbs(url);
        if (bbs == null) {
            log.debug("parse to shitaraba bbs obj failed.");
            callback.onInvalidUrl();
            return;
        }

        ShitarabaService api = retrofit.create(ShitarabaService.class);
        api.getSetting(bbs.getCategory(), bbs.getAddress()).enqueue(new retrofit2.Callback<Setting>() {
            @Override
            public void onResponse(@Nonnull Call<Setting> call, @Nonnull Response<Setting> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(url.toString(), response.body());
                } else {
                    callback.onInvalidUrl();
                }
            }

            @Override
            public void onFailure(@Nonnull Call<Setting> call, @Nonnull Throwable t) {
                log.info(t.getMessage(), t);
                callback.onFail();
            }
        });

    }
}
