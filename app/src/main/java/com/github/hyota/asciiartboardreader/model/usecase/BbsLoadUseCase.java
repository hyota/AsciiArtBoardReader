package com.github.hyota.asciiartboardreader.model.usecase;

import androidx.annotation.NonNull;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;

import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import timber.log.Timber;

@Slf4j
public class BbsLoadUseCase {

    @NonNull
    private BbsRepository bbsRepository;

    @Inject
    public BbsLoadUseCase(@NonNull BbsRepository bbsRepository) {
        this.bbsRepository = bbsRepository;
    }

    public void execute(@NonNull Callback<List<Bbs>> callback, @NonNull Runnable onFail) {
        new Thread(() -> {
            try {
                Timber.d("aaa");
                callback.apply(bbsRepository.findAll());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                onFail.run();
            }
        }).start();
    }

}
