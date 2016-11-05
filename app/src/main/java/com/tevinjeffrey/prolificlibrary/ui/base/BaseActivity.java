package com.tevinjeffrey.prolificlibrary.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @LayoutRes
    protected int layoutId;

    protected abstract void setupPresenter();
    protected abstract void destroyPresenter();
    protected abstract void injectTargets();
    protected abstract void setupToolbar();
    protected abstract void setLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutId();
        setContentView(layoutId);
        injectTargets();
        setupToolbar();
        setupPresenter();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        destroyPresenter();
    }
}
