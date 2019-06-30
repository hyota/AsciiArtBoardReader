package com.github.hyota.asciiartboardreader.presentation.boardlist;

import javax.annotation.Nonnull;

/**
 * 板追加、編集の制約.
 */
public interface BoardCreateEditContract {

    /**
     * View.
     */
    interface View {

        /**
         * 板名を設定する.
         *
         * @param title 板名
         */
        void setBoardTitle(@Nonnull String title);

        void duplicatedError();

        void invalidateError();

    }

    /**
     * プレゼンター
     */
    interface Presenter {

        /**
         * {@link androidx.fragment.app.DialogFragment#onStart()}での処理.
         */
        void onStart();

        /**
         * {@link androidx.fragment.app.DialogFragment#onStop()}での処理.
         */
        void onStop();

        /**
         * 板情報を保存する.
         *
         * @param url   URL
         * @param title 板名
         */
        void save(@Nonnull String url, @Nonnull String title);

        /**
         * 板情報を保存する.
         *
         * @param id    ID
         * @param url   URL
         * @param title 板名
         */
        void save(int id, @Nonnull String url, @Nonnull String title);

        /**
         * 板名をサーバから取得する.
         *
         * @param url URL
         */
        void loadTitleFromServer(@Nonnull String url);

    }

}
