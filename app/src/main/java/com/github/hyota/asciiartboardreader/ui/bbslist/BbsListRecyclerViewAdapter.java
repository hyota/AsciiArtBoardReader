package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.ItemBbsBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.ui.base.ListItemInteractionListener;
import com.github.hyota.asciiartboardreader.ui.common.UpdatableRecyclerViewAdapter;

import java.util.List;

public class BbsListRecyclerViewAdapter extends UpdatableRecyclerViewAdapter<Bbs, BbsListRecyclerViewAdapter.ViewHolder> {

    private ListItemInteractionListener<Bbs> listener;

    public BbsListRecyclerViewAdapter(@NonNull List<Bbs> itemList,
                                      @NonNull ListItemInteractionListener<Bbs> listener) {
        super(itemList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBbsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_bbs, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Bbs bbs = itemList.get(position);
        holder.binding.setBbs(bbs);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(holder.binding.getBbs()));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(holder.binding.getBbs());
            return true;
        });
    }

    @Override
    protected boolean areItemsTheSame(@NonNull Bbs oldItem, @NonNull Bbs newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(@NonNull Bbs oldItem, @NonNull Bbs newItem) {
        return oldItem.equals(newItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemBbsBinding binding;

        ViewHolder(@NonNull ItemBbsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + binding.name.getText() + "'" + " '" + binding.url.getText() + "'";
        }
    }
}
