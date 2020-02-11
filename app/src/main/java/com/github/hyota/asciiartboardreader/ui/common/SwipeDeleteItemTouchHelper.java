package com.github.hyota.asciiartboardreader.ui.common;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class SwipeDeleteItemTouchHelper<ITEM> extends ItemTouchHelper {

    @Setter
    @Getter(AccessLevel.PRIVATE)
    @NonNull
    private List<ITEM> itemList;

    public interface SwipeDeleteInteractionListener<ITEM> {
        void onSwipeDelete(@NonNull ITEM delete);
    }

    public SwipeDeleteItemTouchHelper(@NonNull List<ITEM> itemList, @NonNull RecyclerView.Adapter adapter, @NonNull SwipeDeleteInteractionListener<ITEM> listener) {
        super(new SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //データリストからスワイプしたデータを削除
                ITEM delete = itemList.remove(viewHolder.getAdapterPosition());

                //リストからスワイプしたカードを削除
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // スワイプ削除を通知
                listener.onSwipeDelete(delete);
            }
        });
        this.itemList = itemList;
    }

    private static class SwipeCallback<ITEM> extends SimpleCallback {

        @Setter
        @NonNull
        private List<ITEM> itemList;
        @NonNull
        private RecyclerView.Adapter adapter;
        @NonNull
        private SwipeDeleteInteractionListener<ITEM> listener;

        public SwipeCallback(@NonNull List<ITEM> itemList, @NonNull RecyclerView.Adapter adapter, @NonNull SwipeDeleteInteractionListener<ITEM> listener) {
            super(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.itemList = itemList;
            this.adapter = adapter;
            this.listener = listener;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //データリストからスワイプしたデータを削除
            ITEM delete = itemList.remove(viewHolder.getAdapterPosition());

            //リストからスワイプしたカードを削除
            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            // スワイプ削除を通知
            listener.onSwipeDelete(delete);
        }
    }

}
