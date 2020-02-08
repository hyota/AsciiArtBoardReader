package com.github.hyota.asciiartboardreader.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.databinding.ActivityMainBinding;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.ui.base.BaseActivity;
import com.github.hyota.asciiartboardreader.ui.base.HasActionBar;
import com.github.hyota.asciiartboardreader.ui.base.HasFloatingActionButton;
import com.github.hyota.asciiartboardreader.ui.bbslist.BbsListFragment;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class MainActivity extends BaseActivity
        implements HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener,
        HasActionBar, HasFloatingActionButton, BbsListFragment.Listener {
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject
    MainViewModel viewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        // 初期画面表示時はフラグメントを初期化
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, BbsListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public void setToolbarTitle(@NonNull String title) {
        binding.appBarMain.toolbar.setTitle(title);
    }

    @Override
    public void showFloatingActionButton() {
        binding.appBarMain.fab.show();
    }

    @Override
    public void hideFloatingActionButton() {
        binding.appBarMain.fab.hide();
    }

    @Override
    public void setFloatingActionButtonImageResource(@DrawableRes int resId) {
        Drawable drawable = getDrawable(resId);
        if (drawable != null) {
            drawable.setTint(ContextCompat.getColor(this, android.R.color.white));
            binding.appBarMain.fab.setImageDrawable(drawable);
        }
    }

    @Override
    public void setFloatingActionButtonOnClickListener(@Nullable View.OnClickListener listener) {
        binding.appBarMain.fab.setOnClickListener(listener);
    }

    @Override
    public void onSelectBbs(@NonNull Bbs bbs) {
        // TODO
        Timber.d("select %s, %s", bbs.getName(), bbs.getUrl());
    }

}
