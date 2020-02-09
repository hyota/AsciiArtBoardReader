package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepositoryImpl;
import com.github.hyota.asciiartboardreader.model.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.model.repository.SettingRepositoryImpl;
import com.github.hyota.asciiartboardreader.model.repository.ShitarabaSettingRepository;
import com.github.hyota.asciiartboardreader.model.repository.ShitarabaSettingRepositoryImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    BbsRepository provideBbsRepository(BbsRepositoryImpl repository) {
        return repository;
    }

    @Provides
    SettingRepository provideSettingRepository(SettingRepositoryImpl repository) {
        return repository;
    }

    @Provides
    ShitarabaSettingRepository provideShitarabaSettingRepository(ShitarabaSettingRepositoryImpl repository) {
        return repository;
    }
}
