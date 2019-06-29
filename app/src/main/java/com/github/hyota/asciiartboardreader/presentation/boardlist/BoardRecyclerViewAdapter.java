package com.github.hyota.asciiartboardreader.presentation.boardlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.entity.Board;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 板一覧表示用RecyclerView.Adapter.
 */
public class BoardRecyclerViewAdapter extends RecyclerView.Adapter<BoardRecyclerViewAdapter.ViewHolder> {

    @NonNull
    private final List<Board> items;
    @Nullable
    private final BoardListFragment.OnBoardListFragmentInteractionListener listener;

    BoardRecyclerViewAdapter(@NonNull List<Board> items,
                             @Nullable BoardListFragment.OnBoardListFragmentInteractionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Board item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.url.setText(item.getUrl());
        holder.item = item;

        holder.itemView.setOnClickListener(v -> {
            if (null != listener) {
                listener.onSelectBoard(holder.item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.url)
        TextView url;
        private Board item;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
