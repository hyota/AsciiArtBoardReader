package com.github.hyota.asciiartboardreader.model.usecase;

import androidx.annotation.NonNull;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadBbsUseCase {

    public interface Callback {
        void onSuccess(@NonNull List<Bbs> result);

        void onFail();
    }

    @Nonnull
    private ExecutorService executorService;
    @NonNull
    private BbsRepository bbsRepository;

    @Inject
    public LoadBbsUseCase(@Nonnull ExecutorService executorService, @NonNull BbsRepository bbsRepository) {
        this.executorService = executorService;
        this.bbsRepository = bbsRepository;
    }

    public Future<?> execute(@NonNull Callback callback) {
        return executorService.submit(() -> {
            try {
                callback.onSuccess(bbsRepository.findAll());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                callback.onFail();
            }
        });
    }

}
