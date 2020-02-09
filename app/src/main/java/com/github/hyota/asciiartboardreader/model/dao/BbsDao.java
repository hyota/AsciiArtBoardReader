package com.github.hyota.asciiartboardreader.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.github.hyota.asciiartboardreader.model.db.PathSegmentsTypeConverter;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.PathSegments;

import java.util.List;

@Dao
public interface BbsDao {

    @Query("select * from bbs order by id")
    List<Bbs> findAll();

    @Insert
    void insert(Bbs bbs);

    @Update
    void update(Bbs bbs);

    @Delete
    void delete(Bbs... bbses);

    @Query("SELECT * FROM bbs WHERE title = :title")
    Bbs selectTitleEquals(String title);

    @TypeConverters({PathSegmentsTypeConverter.class})
    @Query("SELECT * FROM bbs WHERE scheme = :scheme AND host = :host AND path = :path")
    Bbs selectUrlEquals(String scheme, String host, PathSegments path);

}
