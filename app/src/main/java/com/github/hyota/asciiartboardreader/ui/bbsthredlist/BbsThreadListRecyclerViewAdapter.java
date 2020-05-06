package com.github.hyota.asciiartboardreader.ui.bbsthredlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.ItemBbsThreadBinding;
import com.github.hyota.asciiartboardreader.model.entity.BbsThread;
import com.github.hyota.asciiartboardreader.ui.base.ListItemInteractionListener;
import com.github.hyota.asciiartboardreader.ui.common.UpdatableRecyclerViewAdapter;

import java.util.List;

public class BbsThreadListRecyclerViewAdapter extends UpdatableRecyclerViewAdapter<BbsThread, BbsThreadListRecyclerViewAdapter.ViewHolder> {

    private ListItemInteractionListener<BbsThread> listener;

    public BbsThreadListRecyclerViewAdapter(@NonNull List<BbsThread> itemList,
                                            @NonNull ListItemInteractionListener<BbsThread> listener) {
        super(itemList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBbsThreadBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_bbs_thread, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        BbsThread bbsThread = itemList.get(position);
        holder.binding.setBbsThread(bbsThread);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(holder.binding.getBbsThread()));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(holder.binding.getBbsThread());
            return true;
        });
    }

    @Override
    protected boolean areItemsTheSame(@NonNull BbsThread oldItem, @NonNull BbsThread newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(@NonNull BbsThread oldItem, @NonNull BbsThread newItem) {
        return oldItem.equals(newItem);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemBbsThreadBinding binding;

        ViewHolder(@NonNull ItemBbsThreadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + binding.name.getText() + "'";
        }
    }
}
