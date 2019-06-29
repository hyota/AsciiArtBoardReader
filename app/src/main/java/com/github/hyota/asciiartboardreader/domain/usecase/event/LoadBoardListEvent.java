package com.github.hyota.asciiartboardreader.domain.usecase.event;

import com.github.hyota.asciiartboardreader.domain.entity.Board;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Getter;

/**
 * 板一覧取得イベント.
 */
public class LoadBoardListEvent {
    /**
     * 全板情報.
     */
    @Getter
    @Nonnull
    final private List<Board> boardList;

    /**
     * コンストラクタ.
     *
     * @param boardList 全板情報
     */
    public LoadBoardListEvent(@Nonnull List<Board> boardList) {
        this.boardList = Collections.unmodifiableList(boardList);
    }
}
