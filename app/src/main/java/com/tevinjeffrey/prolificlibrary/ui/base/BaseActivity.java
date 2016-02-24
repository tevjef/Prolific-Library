package com.tevinjeffrey.prolificlibrary.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected abstract void setupPresenter();
    protected abstract void destroyPresenter();
    protected abstract void injectTargets();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        injectTargets();
        setupPresenter();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        destroyPresenter();
    }
}
