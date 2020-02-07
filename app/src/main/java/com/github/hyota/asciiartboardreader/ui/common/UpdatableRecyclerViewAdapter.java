package com.github.hyota.asciiartboardreader.ui.common;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.AllArgsConstructor;

public abstract class UpdatableRecyclerViewAdapter<ITEM, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    @NonNull
    protected List<ITEM> itemList;

    public UpdatableRecyclerViewAdapter(@NonNull List<ITEM> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void update(@NonNull List<ITEM> newItemList) {
        List<ITEM> oldItemList = itemList;
        itemList = newItemList;
        new UpdateAsyncTask(this, new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItemList.size();
            }

            @Override
            public int getNewListSize() {
                return newItemList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return UpdatableRecyclerViewAdapter.this.areItemsTheSame(oldItemList.get(oldItemPosition), newItemList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return UpdatableRecyclerViewAdapter.this.areContentsTheSame(oldItemList.get(oldItemPosition), newItemList.get(newItemPosition));
            }
        }).execute();
    }

    protected abstract boolean areItemsTheSame(@NonNull ITEM oldItem, @NonNull ITEM newItem);

    protected abstract boolean areContentsTheSame(@NonNull ITEM oldItem, @NonNull ITEM newItem);

    @AllArgsConstructor
    private static class UpdateAsyncTask extends AsyncTask<Void, Void, DiffUtil.DiffResult> {
        @NonNull
        private RecyclerView.Adapter adapter;
        @NonNull
        private DiffUtil.Callback diffCallback;

        @Override
        protected DiffUtil.DiffResult doInBackground(Void... voids) {
            return DiffUtil.calculateDiff(diffCallback);
        }

        @Override
        protected void onPostExecute(DiffUtil.DiffResult diffResult) {
            super.onPostExecute(diffResult);
            diffResult.dispatchUpdatesTo(adapter);
        }

    }

}
