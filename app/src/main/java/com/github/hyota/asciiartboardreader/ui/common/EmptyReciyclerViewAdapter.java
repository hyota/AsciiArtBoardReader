package com.github.hyota.asciiartboardreader.ui.common;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmptyReciyclerViewAdapter extends RecyclerView.Adapter<EmptyReciyclerViewAdapter.EmptyViewHolder> {

    @NonNull
    @Override
    public EmptyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyViewHolder holder, int position) {
        // NOOP
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
