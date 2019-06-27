package com.github.hyota.asciiartboardreader.presentation.main;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.presentation.common.BaseActivity;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainPresenter presenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

}
