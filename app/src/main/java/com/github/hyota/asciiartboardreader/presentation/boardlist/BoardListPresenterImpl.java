package com.github.hyota.asciiartboardreader.presentation.boardlist;

import com.github.hyota.asciiartboardreader.di.FragmentScope;
import com.github.hyota.asciiartboardreader.domain.entity.Board;
import com.github.hyota.asciiartboardreader.domain.usecase.LoadBoardListUseCase;
import com.github.hyota.asciiartboardreader.domain.usecase.event.LoadBoardListEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@FragmentScope
public class BoardListPresenterImpl implements BoardListContract.Presenter {

    @Nonnull
    private BoardListContract.View view;
    @Nonnull
    private LoadBoardListUseCase loadBoardListUseCase;

    private List<Board> list;

    @Inject
    public BoardListPresenterImpl(@Nonnull BoardListContract.View view, @Nonnull LoadBoardListUseCase loadBoardListUseCase) {
        this.view = view;
        this.loadBoardListUseCase = loadBoardListUseCase;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void load() {
        loadBoardListUseCase.execute();
    }

    @Subscribe
    public void onLoadBoardListEvent(@Nonnull LoadBoardListEvent event) {
        view.setBordList(event.getBoardList());
    }

}
