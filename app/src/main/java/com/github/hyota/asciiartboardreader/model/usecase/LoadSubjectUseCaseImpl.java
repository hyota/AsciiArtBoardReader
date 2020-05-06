package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.di.annotation.Local;
import com.github.hyota.asciiartboardreader.di.annotation.Remote;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.net.download.DownloadProgressListener;
import com.github.hyota.asciiartboardreader.model.repository.SubjectRepository;

import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadSubjectUseCaseImpl implements LoadSubjectUseCase {

    @Nonnull
    private ExecutorService executorService;
    @Nonnull
    private SubjectRepository subjectLocalRepository;
    @Nonnull
    private LoadSubjectUseCase loadSubjectRemoteUseCase;

    @Inject
    public LoadSubjectUseCaseImpl(@Nonnull ExecutorService executorService, @Local @Nonnull SubjectRepository subjectLocalRepository, @Nonnull @Remote LoadSubjectUseCase loadSubjectRemoteUseCase) {
        this.executorService = executorService;
        this.subjectLocalRepository = subjectLocalRepository;
        this.loadSubjectRemoteUseCase = loadSubjectRemoteUseCase;
    }

    @Override
    public void executeOnCurrentThread(@Nonnull Bbs bbs, @Nonnull LoadSubjectUseCase.Callback callback, @Nonnull DownloadProgressListener progressListener) {
        subjectLocalRepository.load(bbs, new SubjectRepository.Callback() {
            @Override
            public void onSuccess(@Nonnull Subject subject) {
                log.debug("local subject file load success.");
                callback.onSuccess(subject);
            }

            @Override
            public void onFail() {
                log.warn("local subject file load failed.");
                loadSubjectRemoteUseCase.executeOnCurrentThread(bbs, callback, progressListener);
            }

            @Override
            public void onNotFound() {
                log.debug("local subject file is not found.");
                loadSubjectRemoteUseCase.executeOnCurrentThread(bbs, callback, progressListener);
            }
        });
    }

    @Override
    @Nonnull
    public ExecutorService getExecutorService() {
        return executorService;
    }

}
