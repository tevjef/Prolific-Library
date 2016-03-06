package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.ui.base.BaseView;

public interface NewBookView extends BaseView {
    void showError(Throwable e);
    void addComplete();
}
