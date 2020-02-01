package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.ui.base.BaseFragment;
import com.github.hyota.asciiartboardreader.ui.common.EmptyReciyclerViewAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

public class BbsListFragment extends BaseFragment {

    @Inject
    BbsListViewModel viewModel;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private Listener listener;

    public interface Listener {
        void onSelectBbs(@NonNull Bbs bbs);
    }

    public BbsListFragment() {
    }

    public static BbsListFragment newInstance() {
        BbsListFragment fragment = new BbsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new EmptyReciyclerViewAdapter());
        viewModel.getBbsList().observe(this,
                bbsList -> {
                    Timber.d("bbb");
                    if (bbsList.isSuccess()) {
                        BbsListRecyclerViewAdapter adapter =
                                new BbsListRecyclerViewAdapter(bbsList.getData(), item -> listener.onSelectBbs(item));
                        recyclerView.setAdapter(adapter);
                    } else {
                        showErrorToast(R.string.bbs_list_load_error);
                    }
                });
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            Timber.w("%s is not implemented Listener.", context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @NonNull
    @Override
    protected String getActionBarTitle() {
        return context.getString(R.string.bbs_list_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bbs_list;
    }

    @Override
    protected void initializeFloatingActionButton() {
        if (hasFloatingActionButton != null) {
            hasFloatingActionButton.setFloatingActionButtonImageResource(android.R.drawable.ic_input_add);
            hasFloatingActionButton.setFloatingActionButtonOnClickListener(v -> {
                // TODO
                Timber.d("on click floating action button.");
            });
            hasFloatingActionButton.showFloatingActionButton();
        } else {
            Timber.w("%s is not implemented HasFloatingActionButton", context);
        }
    }
}
