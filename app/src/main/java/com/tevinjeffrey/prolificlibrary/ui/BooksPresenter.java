package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.ui.base.BasePresenter;

import javax.inject.Inject;

public class BooksPresenter extends BasePresenter<BooksView> {
    private final DataManager dataManager;
    @Inject
    public BooksPresenter(DataManager manager) {
        this.dataManager = manager;
    }
}
