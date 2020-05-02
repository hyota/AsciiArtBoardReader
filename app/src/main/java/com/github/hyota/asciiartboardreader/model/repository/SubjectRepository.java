package com.github.hyota.asciiartboardreader.model.repository;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;

import java.io.File;

import javax.annotation.Nonnull;

public interface SubjectRepository {

    interface Callback {

        void onSuccess(@Nonnull Subject subject);

        void onFail();

        void onNotFound();

    }

    void load(@Nonnull Bbs bbs, @Nonnull Callback callback);

    default File getSubjectFile(@Nonnull File storage, @Nonnull Bbs bbs) {
        File dir = new File(storage, bbs.toEncodeUrlString());
        if (!dir.isDirectory() && !dir.mkdirs()) {
            throw new IllegalStateException("mkdir " + dir + " is failed.");
        }
        return new File(dir, "subject.txt");
    }

}
