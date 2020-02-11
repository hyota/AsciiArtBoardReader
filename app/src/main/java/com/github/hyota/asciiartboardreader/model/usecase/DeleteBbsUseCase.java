package com.github.hyota.asciiartboardreader.model.usecase;

import androidx.annotation.NonNull;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeleteBbsUseCase {
    @Nonnull
    private BbsRepository bbsRepository;

    @Inject
    public DeleteBbsUseCase(@Nonnull BbsRepository bbsRepository) {
        this.bbsRepository = bbsRepository;
    }

    public interface Callback {
        void onSuccess();

        void onFail();
    }

    public void execute(@NonNull Bbs bbs, @NonNull Callback callback) {
        new Thread(() -> {
            try {
                bbsRepository.delete(bbs);
                callback.onSuccess();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                callback.onFail();
            }
        }).start();
    }

}
