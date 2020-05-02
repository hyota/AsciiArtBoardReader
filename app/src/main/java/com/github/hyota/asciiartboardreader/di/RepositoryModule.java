package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.di.annotation.Local;
import com.github.hyota.asciiartboardreader.di.annotation.Remote;
import com.github.hyota.asciiartboardreader.di.annotation.Shitaraba;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepository;
import com.github.hyota.asciiartboardreader.model.repository.BbsRepositoryImpl;
import com.github.hyota.asciiartboardreader.model.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.model.repository.SettingRepositoryImpl;
import com.github.hyota.asciiartboardreader.model.repository.ShitarabaSettingRepository;
import com.github.hyota.asciiartboardreader.model.repository.ShitarabaSettingRepositoryImpl;
import com.github.hyota.asciiartboardreader.model.repository.SubjectLocalRepositoryImpl;
import com.github.hyota.asciiartboardreader.model.repository.SubjectRemoteRepositoryImpl;
import com.github.hyota.asciiartboardreader.model.repository.SubjectRepository;
import com.github.hyota.asciiartboardreader.model.repository.SubjectShitarabaRepositoryImpl;

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

    @Local
    @Provides
    SubjectRepository provideSubjectLocalRepository(SubjectLocalRepositoryImpl repository) {
        return repository;
    }

    @Provides
    @Remote
    SubjectRepository provideSubjectRemoteRepository(SubjectRemoteRepositoryImpl repository) {
        return repository;
    }

    @Provides
    @Shitaraba
    SubjectRepository provideSubjectShitarabaRepository(SubjectShitarabaRepositoryImpl repository) {
        return repository;
    }

}
