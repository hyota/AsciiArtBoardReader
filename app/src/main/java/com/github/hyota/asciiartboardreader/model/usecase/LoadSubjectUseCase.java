package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.di.annotation.Local;
import com.github.hyota.asciiartboardreader.di.annotation.Remote;
import com.github.hyota.asciiartboardreader.di.annotation.Shitaraba;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.ShitarabaBbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.net.download.DownloadProgressListener;
import com.github.hyota.asciiartboardreader.model.repository.SubjectRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadSubjectUseCase {

    public interface Callback {
        void onSuccess(@Nonnull Subject subject);

        void onFail();
    }

    @Nonnull
    private ExecutorService executorService;
    @Nonnull
    private SubjectRepository subjectLocalRepository;
    @Nonnull
    private SubjectRepository subjectRemoteRepository;
    @Nonnull
    private SubjectRepository subjectShitarabaRepository;

    @Inject
    public LoadSubjectUseCase(@Nonnull ExecutorService executorService, @Local @Nonnull SubjectRepository subjectLocalRepository, @Nonnull @Remote SubjectRepository subjectRemoteRepository, @Nonnull @Shitaraba SubjectRepository subjectShitarabaRepository) {
        this.executorService = executorService;
        this.subjectLocalRepository = subjectLocalRepository;
        this.subjectRemoteRepository = subjectRemoteRepository;
        this.subjectShitarabaRepository = subjectShitarabaRepository;
    }

    public Future<?> execute(@Nonnull Bbs bbs, boolean force, @Nonnull Callback callback, @Nonnull DownloadProgressListener progressListener) {
        return executorService.submit(() -> {
            if (force) {
                loadFromRemote(bbs, callback, progressListener);
            } else {
                loadFromLocal(bbs, callback, progressListener);
            }
        });
    }

    private void loadFromLocal(@Nonnull Bbs bbs, @Nonnull Callback callback, @Nonnull DownloadProgressListener progressListener) {
        subjectLocalRepository.load(bbs, new SubjectRepository.Callback() {
            @Override
            public void onSuccess(@Nonnull Subject subject) {
                log.debug("local subject file load success.");
                callback.onSuccess(subject);
            }

            @Override
            public void onFail() {
                log.warn("local subject file load failed.");
                loadFromRemote(bbs, callback, progressListener);
            }

            @Override
            public void onNotFound() {
                log.debug("local subject file is not found.");
                loadFromRemote(bbs, callback, progressListener);
            }
        });
    }

    private void loadFromRemote(@Nonnull Bbs bbs, @Nonnull Callback callback, @Nonnull DownloadProgressListener progressListener) {
        SubjectRepository repository;
        if (bbs instanceof ShitarabaBbs) {
            log.debug("load from shitaraba.");
            repository = subjectShitarabaRepository;
        } else {
            log.debug("load from remote.");
            repository = subjectRemoteRepository;
        }
        repository.load(bbs, new SubjectRepository.Callback() {
            @Override
            public void onSuccess(@Nonnull Subject subject) {
                log.debug("load success.");
                callback.onSuccess(subject);
            }

            @Override
            public void onFail() {
                log.debug("remote subject file load failed.");
                callback.onFail();
            }

            @Override
            public void onNotFound() {
                log.debug("remote subject file is not found.");
                callback.onFail();
            }
        }, progressListener);
    }

}
