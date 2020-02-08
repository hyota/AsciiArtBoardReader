package com.github.hyota.asciiartboardreader.model.usecase;

import androidx.annotation.NonNull;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;

import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadBbsUseCase {

    public interface Callback {
        void onSuccess(@NonNull List<Bbs> result);

        void onFail();
    }

    @NonNull
    private BbsRepository bbsRepository;

    @Inject
    public LoadBbsUseCase(@NonNull BbsRepository bbsRepository) {
        this.bbsRepository = bbsRepository;
    }

    public void execute(@NonNull Callback callback) {
        new Thread(() -> {
            try {
                callback.onSuccess(bbsRepository.findAll());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                callback.onFail();
            }
        }).start();
    }

}
