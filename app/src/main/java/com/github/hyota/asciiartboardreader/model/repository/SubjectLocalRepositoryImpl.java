package com.github.hyota.asciiartboardreader.model.repository;

import com.github.hyota.asciiartboardreader.di.annotation.Local;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.net.converter.SubjectConverterFactory;

import java.io.File;
import java.io.FileInputStream;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubjectLocalRepositoryImpl implements SubjectRepository {

    @Nonnull
    private File storage;
    @Nonnull
    private SubjectConverterFactory.SubjectSteamConverter converter;

    @Inject
    public SubjectLocalRepositoryImpl(@Local @Nonnull File storage) {
        this.storage = storage;
    }

    @Override
    public void load(@Nonnull Bbs bbs, @Nonnull Callback callback) {
        File subjectFile = getSubjectFile(storage, bbs);
        if (!subjectFile.isFile()) {
            callback.onNotFound();
            return;
        }
        SubjectConverterFactory.SubjectSteamConverter converter = new SubjectConverterFactory.SubjectSteamConverter(bbs.getCharset());
        try (FileInputStream fis = new FileInputStream(subjectFile)) {
            Subject subject = converter.convert(fis);
            if (subject != null) {
                callback.onSuccess(subject);
            } else {
                callback.onFail();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            callback.onFail();
        }

    }

}
