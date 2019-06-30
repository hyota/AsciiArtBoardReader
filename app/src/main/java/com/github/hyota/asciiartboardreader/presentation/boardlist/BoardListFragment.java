package com.github.hyota.asciiartboardreader.presentation.boardlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.entity.Board;
import com.github.hyota.asciiartboardreader.presentation.common.BaseFragment;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;

/**
 * 板一覧.
 */
public class BoardListFragment extends BaseFragment implements BoardListContract.View {

    @Inject
    BoardListContract.Presenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @Nullable
    private OnBoardListFragmentInteractionListener listener;

    /**
     * コンストラクタ.
     */
    public BoardListFragment() {
    }

    /**
     * ファクトリメソッド.
     *
     * @return BoardListFragment
     */
    public static BoardListFragment newInstance() {
        return new BoardListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBoardListFragmentInteractionListener) {
            listener = (OnBoardListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
        presenter.load();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_board_list;
    }

    @NonNull
    @Override
    protected String getToolbarTitle() {
        return context.getString(R.string.title_board_list);
    }

    @Override
    public void setBordList(@Nonnull List<Board> bordList) {
        recyclerView.setAdapter(new BoardRecyclerViewAdapter(bordList, listener));
    }

    public interface OnBoardListFragmentInteractionListener {
        void onSelectBoard(@NonNull Board item);
    }
}
