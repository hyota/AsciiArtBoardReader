package com.github.hyota.asciiartboardreader.model.repository;

import androidx.annotation.NonNull;

import com.github.hyota.asciiartboardreader.BuildConfig;
import com.github.hyota.asciiartboardreader.model.dao.BbsDao;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;

import java.util.List;

import javax.inject.Inject;

public class BbsRepositoryImpl implements BbsRepository {

    @NonNull
    private BbsDao dao;

    @Inject
    public BbsRepositoryImpl(@NonNull BbsDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public List<Bbs> findAll() {
        if (BuildConfig.DEBUG) { // TODO 削除
            List<Bbs> bbsList = dao.findAll();
            if (bbsList.isEmpty()) {
                insert(new Bbs("やる夫スレヒロイン板", "http://jbbs.shitaraba.net/otaku/12766/"));
                insert(new Bbs("ゑれぼす板・桜", "http://erebos.sakura.ne.jp/BBS/"));
                return dao.findAll();
            }
        }
        return dao.findAll();
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
}
