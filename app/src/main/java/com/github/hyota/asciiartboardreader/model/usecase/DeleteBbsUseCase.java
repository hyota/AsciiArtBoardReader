package com.github.hyota.asciiartboardreader.model.usecase;

import androidx.annotation.NonNull;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeleteBbsUseCase {

    @Nonnull
    private ExecutorService executorService;
    @Nonnull
    private BbsRepository bbsRepository;

    @Inject
    public DeleteBbsUseCase(@Nonnull ExecutorService executorService, @Nonnull BbsRepository bbsRepository) {
        this.executorService = executorService;
        this.bbsRepository = bbsRepository;
    }

    public interface Callback {
        void onSuccess();

        void onFail();
    }

    public Future<?> execute(@NonNull Bbs bbs, @NonNull Callback callback) {
        return executorService.submit(() -> {
            try {
                bbsRepository.delete(bbs);
                callback.onSuccess();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                callback.onFail();
            }
        });
    }

}
