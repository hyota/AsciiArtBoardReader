package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.Setting;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

@Slf4j
public abstract class InsertUpdateBbsUseCase {

    @Nonnull
    private ValidateBbsUrlUseCase validateBbsUrlUseCase;
    @Nonnull
    protected BbsRepository bbsRepository;

    public interface Callback {

        void onSuccess();

        void onInvalidUrl();

        void onDuplicatedTitle();

        void onDuplicatedUrl(@Nonnull String validatedUrl);

        void onFail();

    }

    protected InsertUpdateBbsUseCase(@Nonnull ValidateBbsUrlUseCase validateBbsUrlUseCase, @Nonnull BbsRepository bbsRepository) {
        this.validateBbsUrlUseCase = validateBbsUrlUseCase;
        this.bbsRepository = bbsRepository;
    }

    public void execute(@Nonnull String title, @Nonnull String url, @Nullable Bbs old, @Nonnull Callback callback) {
        new Thread(() -> validateBbsUrlUseCase.execute(url, new ValidateBbsUrlUseCase.Callback() {
            @Override
            public void onSuccess(@Nonnull String validatedUrl, @Nonnull Setting setting) {
                validateAndSave(title, validatedUrl, old, callback);
            }

            @Override
            public void onInvalidUrl() {
                callback.onInvalidUrl();
            }

            @Override
            public void onFail() {
                callback.onFail();
            }
        })).start();
    }

    protected void validateAndSave(@Nonnull String title, @Nonnull String url, @Nullable Bbs old, @Nonnull Callback callback) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            callback.onInvalidUrl();
            return;
        }


        Bbs entity = createBbs(title, httpUrl, old);
        Bbs selectTitleEquals = bbsRepository.selectTitleEquals(entity);
        if (selectTitleEquals != null && selectTitleEquals.getId() != entity.getId()) {
            callback.onDuplicatedTitle();
            return;
        }
        Bbs selectUrlEquals = bbsRepository.selectUrlEquals(entity);
        if (selectUrlEquals != null && selectUrlEquals.getId() != entity.getId()) {
            callback.onDuplicatedUrl(entity.getHttpUrl().toString());
            return;
        }
        try {
            save(entity, callback);
            callback.onSuccess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            callback.onFail();
        }
    }

    protected abstract Bbs createBbs(@Nonnull String title, @Nonnull HttpUrl url, @Nullable Bbs old);

    protected abstract void save(@Nonnull Bbs bbs, @Nonnull Callback callback);

    public static class InsertBbsUseCase extends InsertUpdateBbsUseCase {

        @Inject
        public InsertBbsUseCase(@Nonnull ValidateBbsUrlUseCase validateBbsUrlUseCase, @Nonnull BbsRepository bbsRepository) {
            super(validateBbsUrlUseCase, bbsRepository);
        }

        @Override
        protected Bbs createBbs(@Nonnull String title, @Nonnull HttpUrl url, @Nullable Bbs old) {
            return new Bbs(title, url);
        }

        @Override
        protected void save(@Nonnull Bbs bbs, @Nonnull Callback callback) {
            bbsRepository.insert(bbs);
        }
    }

    public static class UpdateBbsUseCase extends InsertUpdateBbsUseCase {

        @Inject
        public UpdateBbsUseCase(@Nonnull ValidateBbsUrlUseCase validateBbsUrlUseCase, @Nonnull BbsRepository bbsRepository) {
            super(validateBbsUrlUseCase, bbsRepository);
        }

        @Override
        protected Bbs createBbs(@Nonnull String title, @Nonnull HttpUrl url, @Nullable Bbs old) {
            long oldId = 0;
            if (old != null) {
                oldId = old.getId();
            }
            return new Bbs(oldId, title, url);
        }

        @Override
        protected void save(@Nonnull Bbs bbs, @Nonnull Callback callback) {
            bbsRepository.update(bbs);
        }
    }
}
