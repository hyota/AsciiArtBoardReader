package com.github.hyota.asciiartboardreader.presentation.boardlist;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.entity.Board;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * 板一覧表示用RecyclerView.Adapter.
 */
public class BoardRecyclerViewAdapter
        extends RecyclerView.Adapter<BoardRecyclerViewAdapter.ViewHolder> {

    @NonNull
    private final Context context;
    @NonNull
    private final List<Board> items;
    @NonNull
    private final Listener listener;

    public interface Listener {
        void onItemClick(@NonNull Board board);

        void onEdit(@NonNull Board board);

        void onDelete(@NonNull Board board);
    }

    BoardRecyclerViewAdapter(@NonNull Context context, @NonNull List<Board> items,
                             @NonNull Listener listener) {
        this.context = context;
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

        holder.itemView.setOnClickListener(v -> listener.onItemClick(holder.item));
        holder.menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.popup_menu_board_list_item);
            popupMenu.setGravity(Gravity.END);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.edit) {
                    listener.onEdit(holder.item);
                    return true;
                } else if (menuItem.getItemId() == R.id.delete) {
                    listener.onDelete(holder.item);
                    return true;
                }
                Timber.w("no defined id. id = %d", menuItem.getItemId());
                return false;
            });
            popupMenu.show();
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
        @BindView(R.id.menu)
        ImageButton menu;
        private Board item;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            menu.setImageDrawable(
                    new IconDrawable(context, FontAwesomeIcons.fa_ellipsis_v)
                            .colorRes(android.R.color.darker_gray).sizeDp(30));
        }
    }
}
