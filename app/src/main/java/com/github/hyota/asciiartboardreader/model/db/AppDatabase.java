package com.github.hyota.asciiartboardreader.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.github.hyota.asciiartboardreader.BuildConfig;
import com.github.hyota.asciiartboardreader.model.dao.BbsDao;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;

@Database(entities = {Bbs.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = BuildConfig.APPLICATION_ID + ".database";

    public abstract BbsDao getBbsDao();
}