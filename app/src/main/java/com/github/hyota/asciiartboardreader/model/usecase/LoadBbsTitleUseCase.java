package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.model.entity.Setting;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadBbsTitleUseCase {

    @Nonnull
    private ValidateBbsUrlUseCase validateBbsUrlUseCase;

    public interface Callback {

        void onSuccess(@Nonnull String validateUrl, @Nonnull String loadedTitle);

        void onInvalidUrl();

        void onFail();

    }

    @Inject
    public LoadBbsTitleUseCase(@Nonnull ValidateBbsUrlUseCase validateBbsUrlUseCase) {
        this.validateBbsUrlUseCase = validateBbsUrlUseCase;
    }

    public void execute(@Nonnull String url, @Nonnull Callback callback) {
        validateBbsUrlUseCase.execute(url, new ValidateBbsUrlUseCase.Callback() {
            @Override
            public void onSuccess(@Nonnull String validatedUrl, @Nonnull Setting setting) {
                callback.onSuccess(validatedUrl, setting.getBbsTitle());
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
