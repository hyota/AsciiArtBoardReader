package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.data.repository.BoardRepository;
import com.github.hyota.asciiartboardreader.data.repository.mock.MockBoardRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * リポジトリー用モジュール.
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    BoardRepository provideBoardRepository(MockBoardRepository repository) {
        return repository;
    }

}
