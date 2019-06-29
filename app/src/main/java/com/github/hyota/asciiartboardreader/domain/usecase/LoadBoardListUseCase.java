package com.github.hyota.asciiartboardreader.domain.usecase;

import com.github.hyota.asciiartboardreader.data.repository.BoardRepository;
import com.github.hyota.asciiartboardreader.domain.entity.Board;
import com.github.hyota.asciiartboardreader.domain.usecase.event.ErrorEvent;
import com.github.hyota.asciiartboardreader.domain.usecase.event.LoadBoardListEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 板一覧読み込みUseCase.
 */
public class LoadBoardListUseCase {

    /**
     * 板レポジトリ.
     */
    @Nonnull
    private BoardRepository repository;

    /**
     * コンストラクタ.
     *
     * @param repository 板レポジトリ
     */
    @Inject
    public LoadBoardListUseCase(@Nonnull BoardRepository repository) {
        this.repository = repository;
    }

    /**
     * 実行.
     */
    public void execute() {
        @SuppressWarnings("unused")
        Disposable subscribe = Single.<List<Board>>create(source -> source.onSuccess(repository.findAll()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        boardList -> EventBus.getDefault().post(new LoadBoardListEvent(boardList)),
                        throwable -> EventBus.getDefault().post(new ErrorEvent(throwable))
                );
    }


}
