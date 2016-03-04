package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BaseView;

import java.util.List;

public interface SingleBookView extends BaseView {
    void showError(Throwable e);
    void setData(Book books);
    void showLoading(boolean isLoading);
    void checkoutSuccess();
    void checkoutFail();
}
