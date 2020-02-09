package com.github.hyota.asciiartboardreader.di;

import android.content.Context;

import androidx.room.Room;

import com.github.hyota.asciiartboardreader.model.dao.BbsDao;
import com.github.hyota.asciiartboardreader.model.db.AppDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME)
                .build();
    }

    @Provides
    BbsDao provideBbsDao(AppDatabase database) {
        return database.getBbsDao();
    }

}
