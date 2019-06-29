package com.github.hyota.asciiartboardreader.presentation.boardlist;

import com.github.hyota.asciiartboardreader.domain.entity.Board;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * {@link BoardListFragment}の制約.
 */
public interface BoardListContract {

    /**
     * View.
     */
    interface View {

        /**
         * 板一覧を設定する.
         *
         * @param bordList 板情報のリスト
         */
        void setBordList(@Nonnull List<Board> bordList);

    }

    /**
     * Presenter
     */
    interface Presenter {

        /**
         * {@link androidx.fragment.app.Fragment#onStart()}時の処理.
         */
        void onStart();

        /**
         * {@link androidx.fragment.app.Fragment#onStop()}時の処理.
         */
        void onStop();

        /**
         * 板一覧の読み込み処理.
         */
        void load();

    }

}
