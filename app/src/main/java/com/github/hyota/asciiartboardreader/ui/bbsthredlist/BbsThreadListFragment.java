package com.github.hyota.asciiartboardreader.ui.bbsthredlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.FragmentBbsThreadListBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.ThreadInfo;
import com.github.hyota.asciiartboardreader.model.value.LoadingStateValue;
import com.github.hyota.asciiartboardreader.ui.base.BaseFragment;
import com.github.hyota.asciiartboardreader.ui.base.ListItemInteractionListener;
import com.github.hyota.asciiartboardreader.ui.common.EmptyReciyclerViewAdapter;

import javax.annotation.Nonnull;

import timber.log.Timber;

public class BbsThreadListFragment extends BaseFragment<BbsThreadListViewModel> {

    private static final String ARG_BBS = "bbs";

    private FragmentBbsThreadListBinding binding;
    private Listener listener;
    private BbsThreadListRecyclerViewAdapter adapter;

    public interface Listener {
        void onSelectBbsThread(@Nonnull Bbs bbs, @NonNull ThreadInfo threadInfo);
    }

    public BbsThreadListFragment() {
    }

    public static BbsThreadListFragment newInstance(@Nonnull Bbs bbs) {
        BbsThreadListFragment fragment = new BbsThreadListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BBS, bbs);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            if (args == null) {
                throw new IllegalStateException("args is null!");
            }
            Bbs bbs = (Bbs) args.getSerializable(ARG_BBS);
            viewModel.setBbs(bbs);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bbs_thread_list, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(itemDecoration);
        binding.list.setAdapter(new EmptyReciyclerViewAdapter());
        viewModel.getLoadingState()
                .observe(getViewLifecycleOwner(), state -> binding.swipyrefreshlayout.setRefreshing(LoadingStateValue.isLoading(state)));
        viewModel.getProgressState()
                .observe(getViewLifecycleOwner(), progress -> {
                    Timber.d("progress %s", progress);
                    if (progress <= 0 || 100 <= progress) {
                        binding.progress.setVisibility(View.GONE);
                    } else {
                        binding.progress.setProgress(progress);
                        binding.progress.setVisibility(View.VISIBLE);
                    }
                });
        viewModel.getSubject().observe(getViewLifecycleOwner(), subject -> {
            if (adapter != null) {
                adapter.update(subject.getThreadInfoList());
            } else {
                adapter = new BbsThreadListRecyclerViewAdapter(subject.getThreadInfoList(),
                        new ListItemInteractionListener<ThreadInfo>() {
                            @Override
                            public void onItemClick(@NonNull ThreadInfo threadInfo) {
                                listener.onSelectBbsThread(viewModel.getBbs(), threadInfo);
                            }

                            @Override
                            public void onItemLongClick(@NonNull ThreadInfo threadInfo) {
                                // TODO
                            }
                        });
                binding.list.setAdapter(adapter);
            }
        });
        binding.swipyrefreshlayout.setOnRefreshListener(direction -> viewModel.loadFromRemote());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.list.setAdapter(null);
        adapter = null;
    }

    @NonNull
    @Override
    protected String getActionBarTitle() {
        return viewModel.getBbs().getTitle();
    }

    @NonNull
    @Override
    protected Class<BbsThreadListViewModel> getViewModelClass() {
        return BbsThreadListViewModel.class;
    }
}
