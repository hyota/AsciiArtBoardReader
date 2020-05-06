package com.github.hyota.asciiartboardreader.model.usecase;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.net.download.DownloadProgressListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

public interface LoadSubjectUseCase {

    interface Callback {
        void onSuccess(@Nonnull Subject subject);

        void onFail();
    }

    @Nonnull
    default Future<?> execute(@Nonnull Bbs bbs, @Nonnull Callback callback, @Nonnull DownloadProgressListener progressListener) {
        return getExecutorService().submit(() -> executeOnCurrentThread(bbs, callback, progressListener));
    }

    void executeOnCurrentThread(@Nonnull Bbs bbs, @Nonnull Callback callback, @Nonnull DownloadProgressListener progressListener);

    @Nonnull
    ExecutorService getExecutorService();

}
