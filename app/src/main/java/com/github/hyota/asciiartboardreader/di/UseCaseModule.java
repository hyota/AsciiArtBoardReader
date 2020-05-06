package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.di.annotation.Local;
import com.github.hyota.asciiartboardreader.di.annotation.Remote;
import com.github.hyota.asciiartboardreader.model.usecase.LoadSubjectRemoteUseCaseImpl;
import com.github.hyota.asciiartboardreader.model.usecase.LoadSubjectUseCase;
import com.github.hyota.asciiartboardreader.model.usecase.LoadSubjectUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCaseModule {

    @Local
    @Provides
    LoadSubjectUseCase provideLoadSubjectLocalUseCase(LoadSubjectUseCaseImpl useCase) {
        return useCase;
    }

    @Provides
    @Remote
    LoadSubjectUseCase provideLoadSubjectRemoteUseCase(LoadSubjectRemoteUseCaseImpl useCase) {
        return useCase;
    }

}
