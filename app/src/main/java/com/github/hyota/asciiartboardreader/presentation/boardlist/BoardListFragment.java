package com.github.hyota.asciiartboardreader.presentation.boardlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.entity.Board;
import com.github.hyota.asciiartboardreader.domain.value.AlertDialogRequestCode;
import com.github.hyota.asciiartboardreader.presentation.common.AlertDialogFragment;
import com.github.hyota.asciiartboardreader.presentation.common.BaseFragment;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

/**
 * 板一覧.
 */
public class BoardListFragment extends BaseFragment
        implements BoardListContract.View, BoardCreateEditDialogFragment.Listener,
        AlertDialogFragment.Listener {

    @Inject
    BoardListContract.Presenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    private OnBoardListFragmentInteractionListener listener;
    private RecyclerView.Adapter adapter;


    public interface OnBoardListFragmentInteractionListener {
        void onSelectBoard(@NonNull Board item);
    }

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

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

//        if (floatingActionButtonCallback != null) {
//            floatingActionButtonCallback.setOnClickListener(v -> showAddDialog());
//            floatingActionButtonCallback.setImage(android.R.drawable.ic_input_add);
//        }

        setHasOptionsMenu(true);

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
        adapter = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Timber.d("onCreateOptionMenu");
        inflater.inflate(R.menu.menu_board_list, menu);
        menu.findItem(R.id.add).setIcon(
                new IconDrawable(context, FontAwesomeIcons.fa_plus)
                        .colorRes(android.R.color.white)
                        .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            showAddDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        adapter = new BoardRecyclerViewAdapter(context, bordList,
                new BoardRecyclerViewAdapter.Listener() {
                    @Override
                    public void onItemClick(@NonNull Board board) {
                        listener.onSelectBoard(board);
                    }

                    @Override
                    public void onEdit(@NonNull Board board) {
                        BoardCreateEditDialogFragment.show(getChildFragmentManager(), board);
                    }

                    @Override
                    public void onDelete(@NonNull Board board) {
                        new AlertDialogFragment.Builder(context, getChildFragmentManager())
                                .title(R.string.title_board_delete)
                                .message(R.string.message_board_delete)
                                .cancelable(true)
                                .positive(android.R.string.ok)
                                .negative(android.R.string.cancel)
                                .requestCode(AlertDialogRequestCode.BOARD_DELETE)
                                .show();
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSucceeded() {
        Timber.d("onSucceeded");
    }

    @Override
    public void onCanceled() {
        Timber.d("onCanceled");
    }

    @Override
    public void onAlertDialogSucceeded(@Nullable AlertDialogRequestCode requestCode, int resultCode,
                                       Bundle params) {
        // TODO
    }

    @Override
    public void onAlertDialogCancelled(@Nullable AlertDialogRequestCode requestCode,
                                       Bundle params) {
        // TODO
    }

    /**
     * 板追加ダイアログを表示する.
     */
    private void showAddDialog() {
        Timber.d("showAddDialog");
        BoardCreateEditDialogFragment.show(getChildFragmentManager());
    }

}
