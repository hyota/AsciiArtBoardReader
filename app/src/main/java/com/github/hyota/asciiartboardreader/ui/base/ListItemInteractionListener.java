package com.github.hyota.asciiartboardreader.ui.base;

import androidx.annotation.NonNull;

public interface ListItemInteractionListener<ITEM> {

    void onItemClick(@NonNull ITEM item);

    void onItemLongClick(@NonNull ITEM item);

}
