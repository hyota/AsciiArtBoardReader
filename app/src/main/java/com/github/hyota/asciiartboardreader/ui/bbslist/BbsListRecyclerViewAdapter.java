package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.ui.base.OnItemSelectListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BbsListRecyclerViewAdapter extends RecyclerView.Adapter<BbsListRecyclerViewAdapter.ViewHolder> {

    @NonNull
    private final List<Bbs> itemList;
    @NonNull
    private final OnItemSelectListener<Bbs> listener;

    public BbsListRecyclerViewAdapter(@NonNull List<Bbs> itemList,
                                      @NonNull OnItemSelectListener<Bbs> listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bbs_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = itemList.get(position);
        holder.name.setText(itemList.get(position).getName());

        holder.view.setOnClickListener(v -> listener.onItemSelect(holder.mItem));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        @BindView(R.id.name)
        TextView name;
        Bbs mItem;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
