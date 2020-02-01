package com.github.hyota.asciiartboardreader.model.repository;

import androidx.annotation.NonNull;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;

import java.util.List;

public interface BbsRepository {

    @NonNull
    List<Bbs> findAll();

    void insert(@NonNull Bbs bbs);

    void update(@NonNull Bbs bbs);

    void delete(@NonNull Bbs... bbses);

}
