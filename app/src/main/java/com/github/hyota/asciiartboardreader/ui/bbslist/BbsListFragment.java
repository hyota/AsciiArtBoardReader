package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.FragmentBbsListBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.ui.base.BaseFragment;
import com.github.hyota.asciiartboardreader.ui.base.ListItemInteractionListener;
import com.github.hyota.asciiartboardreader.ui.common.EmptyReciyclerViewAdapter;

import javax.inject.Inject;

import timber.log.Timber;

public class BbsListFragment extends BaseFragment<BbsListViewModel> {

    @Inject
    BbsListViewModel viewModel;
    private FragmentBbsListBinding binding;
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            Timber.w("%s is not implemented Listener.", context);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bbs_list, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.list.setLayoutManager(new LinearLayoutManager(context));
        binding.list.setAdapter(new EmptyReciyclerViewAdapter());
        viewModel.getBbsList().observe(getViewLifecycleOwner(), bbsList -> {
            BbsListRecyclerViewAdapter adapter = new BbsListRecyclerViewAdapter(bbsList,
                    new ListItemInteractionListener<Bbs>() {
                        @Override
                        public void onItemClick(@NonNull Bbs bbs) {
                            listener.onSelectBbs(bbs);
                        }

                        @Override
                        public void onItemLongClick(@NonNull Bbs bbs) {
                            BbsAddEditDialogFragment.show(getChildFragmentManager(), bbs);
                        }
                    });
            binding.list.setAdapter(adapter);
        });
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

    @NonNull
    @Override
    protected Class<BbsListViewModel> getViewModelClass() {
        return BbsListViewModel.class;
    }

    @Override
    protected void initializeFloatingActionButton() {
        if (hasFloatingActionButton != null) {
            hasFloatingActionButton.setFloatingActionButtonImageResource(android.R.drawable.ic_input_add);
            hasFloatingActionButton.setFloatingActionButtonOnClickListener(v -> BbsAddEditDialogFragment.show(getChildFragmentManager()));
            hasFloatingActionButton.showFloatingActionButton();
        } else {
            Timber.w("%s is not implemented HasFloatingActionButton", context);
        }
    }
}
