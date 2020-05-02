package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.FragmentBbsListBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.value.AlertDialogRequestCodeValue;
import com.github.hyota.asciiartboardreader.ui.base.BaseFragment;
import com.github.hyota.asciiartboardreader.ui.base.ListItemInteractionListener;
import com.github.hyota.asciiartboardreader.ui.common.AlertDialogFragment;
import com.github.hyota.asciiartboardreader.ui.common.EmptyReciyclerViewAdapter;
import com.github.hyota.asciiartboardreader.ui.common.SwipeDeleteItemTouchCallback;

import timber.log.Timber;

public class BbsListFragment extends BaseFragment<BbsListViewModel>
        implements BbsAddEditDialogFragment.Listener {
    private static final String DIALOG_ARG_BBS = "dialogArgBbs";

    private FragmentBbsListBinding binding;
    private Listener listener;
    private BbsListRecyclerViewAdapter adapter;
    private SwipeDeleteItemTouchCallback<Bbs> swipeDeleteItemTouchCallback;

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
        Timber.d("onViewCreated.");
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(itemDecoration);
        binding.list.setAdapter(new EmptyReciyclerViewAdapter());
        viewModel.getBbsList().observe(getViewLifecycleOwner(), bbsList -> {
            Timber.d("adapter %s", adapter);
            if (adapter != null) {
                adapter.update(bbsList);
                swipeDeleteItemTouchCallback.setItemList(bbsList);
            } else {
                adapter = new BbsListRecyclerViewAdapter(bbsList,
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
                swipeDeleteItemTouchCallback = new SwipeDeleteItemTouchCallback<>(bbsList, adapter, delete -> {
                    Bundle params = new Bundle();
                    params.putSerializable(DIALOG_ARG_BBS, delete);
                    new AlertDialogFragment.Builder(this)
                            .setTitle(R.string.confirm_dialog_title)
                            .setMessage(R.string.bbs_delete_confirm_message)
                            .setPositive(android.R.string.ok)
                            .setNegative(android.R.string.cancel)
                            .setCancelable(false)
                            .setRequestCode(AlertDialogRequestCodeValue.BBS_DELETE_CONFIRM)
                            .setParams(params)
                            .show();
                });
                new ItemTouchHelper(swipeDeleteItemTouchCallback).attachToRecyclerView(binding.list);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.list.setAdapter(null);
        adapter = null;
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
    public void onAlertDialogSucceeded(@AlertDialogRequestCodeValue.AlertDialogRequestCode int requestCode, int resultCode, Bundle params) {
        if (requestCode == AlertDialogRequestCodeValue.BBS_DELETE_CONFIRM) {
            if (resultCode == DialogInterface.BUTTON_POSITIVE) {
                Bbs delete = (Bbs) params.getSerializable(DIALOG_ARG_BBS);
                if (delete != null) {
                    viewModel.delete(delete);
                } else {
                    Timber.w("delete target is null.");
                    viewModel.load();
                }
            } else {
                viewModel.load();
            }
        } else {
            super.onAlertDialogSucceeded(requestCode, resultCode, params);
        }
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

    @Override
    public void onBbsAddEditComplete() {
        viewModel.load();
    }

}
