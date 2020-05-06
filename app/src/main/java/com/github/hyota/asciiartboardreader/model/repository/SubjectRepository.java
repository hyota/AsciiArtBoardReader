package com.github.hyota.asciiartboardreader.model.repository;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.net.download.DownloadProgressListener;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface SubjectRepository {

    interface Callback {

        void onSuccess(@Nonnull Subject subject);

        void onFail();

        void onNotFound();

    }

    void load(@Nonnull Bbs bbs, @Nonnull Callback callback, @Nullable DownloadProgressListener progressListener);

    default void load(@Nonnull Bbs bbs, @Nonnull Callback callback) {
        load(bbs, callback, null);
    }

    default File getSubjectFile(@Nonnull File storage, @Nonnull Bbs bbs) {
        File dir = new File(storage, bbs.toEncodeUrlString());
        if (!dir.isDirectory() && !dir.mkdirs()) {
            throw new IllegalStateException("mkdir " + dir + " is failed.");
        }
        return new File(dir, "subject.txt");
    }

}
