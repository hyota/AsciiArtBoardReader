package com.github.hyota.asciiartboardreader.data.repository.mock;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.BoardRepository;
import com.github.hyota.asciiartboardreader.domain.entity.Board;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

public class MockBoardRepository implements BoardRepository {

    @Inject
    public MockBoardRepository() {
    }

    @Nullable
    @Override
    public Board findOne(Integer integer) {
        return new Board(0, "http://jbbs.shitaraba.net/otaku/15956/", "やる夫スレヒロイン板（仮）");
    }

    @Override
    public boolean exists(Integer integer) {
        return true;
    }

    @Nonnull
    @Override
    public List<Board> findAll() {
        return IntStream.range(0, 10)
                .boxed()
                .flatMap(i ->
                        Stream.of(
                                new Board(0, "http://jbbs.shitaraba.net/otaku/15956/", "やる夫スレヒロイン板（仮）"),
                                new Board(1, "http://yaruoshelter.com/yaruo001/", "やる夫板のシェルター")
                        )
                ).toList();
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Nullable
    @Override
    public Board save(@Nonnull Board entity) {
        return entity;
    }

    @Override
    public void delete(@Nonnull Board entity) {

    }
}
