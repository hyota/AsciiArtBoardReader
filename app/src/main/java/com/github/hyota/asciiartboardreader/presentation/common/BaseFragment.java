package com.github.hyota.asciiartboardreader.presentation.common;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

/**
 * Fragmentの親クラス.
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    protected Context context;
    @Nullable
    private ToolbarCallback toolbarCallback;
    @Nullable
    protected FloatingActionButtonCallback floatingActionButtonCallback;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.context = context;
        if (context instanceof ToolbarCallback) {
            this.toolbarCallback = (ToolbarCallback) context;
        }
        if (context instanceof FloatingActionButtonCallback) {
            this.floatingActionButtonCallback = (FloatingActionButtonCallback) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (toolbarCallback != null) {
            toolbarCallback.setTitle(getToolbarTitle());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    /**
     * レイアウトリソースIDを取得する.
     *
     * @return レイアウトリソースID
     */
    @LayoutRes
    protected abstract int getLayoutRes();

    /**
     * タイトルを取得する.
     *
     * @return タイトル
     */
    @NonNull
    protected abstract String getToolbarTitle();
}
