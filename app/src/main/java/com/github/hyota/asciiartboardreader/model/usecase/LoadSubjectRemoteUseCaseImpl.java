package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.di.annotation.Remote;
import com.github.hyota.asciiartboardreader.di.annotation.Shitaraba;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.ShitarabaBbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.net.download.DownloadProgressListener;
import com.github.hyota.asciiartboardreader.model.repository.SubjectRepository;

import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadSubjectRemoteUseCaseImpl implements LoadSubjectUseCase {

    @Nonnull
    private ExecutorService executorService;
    @Nonnull
    private SubjectRepository subjectRemoteRepository;
    @Nonnull
    private SubjectRepository subjectShitarabaRepository;

    @Inject
    public LoadSubjectRemoteUseCaseImpl(@Nonnull ExecutorService executorService, @Nonnull @Remote SubjectRepository subjectRemoteRepository, @Nonnull @Shitaraba SubjectRepository subjectShitarabaRepository) {
        this.executorService = executorService;
        this.subjectRemoteRepository = subjectRemoteRepository;
        this.subjectShitarabaRepository = subjectShitarabaRepository;
    }

    @Override
    public void executeOnCurrentThread(@Nonnull Bbs bbs, @Nonnull Callback callback, @Nonnull DownloadProgressListener progressListener) {
        getRemoteRepository(bbs).load(bbs, new SubjectRepository.Callback() {
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

    @Override
    @Nonnull
    public ExecutorService getExecutorService() {
        return executorService;
    }

    private SubjectRepository getRemoteRepository(Bbs bbs) {
        return subjectRemoteRepository;
    }

    private SubjectRepository getRemoteRepository(ShitarabaBbs bbs) {
        return subjectShitarabaRepository;
    }

}
