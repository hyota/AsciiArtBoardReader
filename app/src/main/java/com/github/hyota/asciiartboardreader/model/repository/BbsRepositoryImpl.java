package com.github.hyota.asciiartboardreader.model.repository;

import androidx.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.BuildConfig;
import com.github.hyota.asciiartboardreader.model.dao.BbsDao;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.utils.ShitarabaUtils;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class BbsRepositoryImpl implements BbsRepository {

    @NonNull
    private BbsDao dao;
    @Nonnull
    private ShitarabaUtils shitarabaUtils;

    @Inject
    public BbsRepositoryImpl(@NonNull BbsDao dao, @Nonnull ShitarabaUtils shitarabaUtils) {
        this.dao = dao;
        this.shitarabaUtils = shitarabaUtils;
    }

    @NonNull
    @Override
    public List<Bbs> findAll() {
        if (BuildConfig.DEBUG) { // TODO 削除
            List<Bbs> bbsList = dao.findAll();
            if (bbsList.isEmpty()) {
                insert(new Bbs("やる夫スレヒロイン板", "http", "bbs.shitaraba.net", Arrays.asList("otaku", "12766")));
                insert(new Bbs("ゑれぼす板・桜", "http", "erebos.sakura.ne.jp", Arrays.asList("BBS")));
                return dao.findAll();
            }
        }
        return Stream.of(dao.findAll()).map(shitarabaUtils::convert).collect(Collectors.toList());
    }

    @Override
    public void insert(@NonNull Bbs bbs) {
        dao.insert(bbs);
    }

    @Override
    public void update(@NonNull Bbs bbs) {
        dao.update(bbs);
    }

    @Override
    public void delete(@NonNull Bbs... bbses) {
        dao.delete(bbses);
    }

    @Override
    public Bbs selectTitleEquals(@Nonnull Bbs bbs) {
        return shitarabaUtils.convert(dao.selectTitleEquals(bbs.getTitle()));
    }

    @Override
    public Bbs selectUrlEquals(@Nonnull Bbs bbs) {
        return shitarabaUtils.convert(dao.selectUrlEquals(bbs.getScheme(), bbs.getHost(), bbs.getPath()));
    }
}
