package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepositoryImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    BbsRepository provideBbsRepository(BbsRepositoryImpl repository) {
        return repository;
    }

}
