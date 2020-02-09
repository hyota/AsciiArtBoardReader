package com.github.hyota.asciiartboardreader.model.repository;

import com.github.hyota.asciiartboardreader.model.entity.Setting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.HttpUrl;

public interface SettingRepository {

    interface Callback {

        void onSuccess(@Nonnull String validatedUrl, @Nullable Setting setting);

        void onInvalidUrl();

        void onFail();

    }


    void load(@Nonnull HttpUrl httpUrl, @Nonnull Callback callback);

}
