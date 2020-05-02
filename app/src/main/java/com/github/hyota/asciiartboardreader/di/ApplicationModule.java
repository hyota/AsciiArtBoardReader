package com.github.hyota.asciiartboardreader.di;

import android.content.Context;

import com.github.hyota.asciiartboardreader.MyApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    Context provideContext(MyApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    ExecutorService provideExecutorService() {
        return Executors.newCachedThreadPool(new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "cachedThread #" + mCount.getAndIncrement());
            }
        });
    }

}
