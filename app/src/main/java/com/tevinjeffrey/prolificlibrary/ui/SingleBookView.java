package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BaseView;

public interface SingleBookView extends BaseView {
    void showError(Throwable e);
    void setData(Book updatedBook);
    void showLoading(boolean isLoading);
    void checkoutSuccess();
    void checkoutFail();
    void deleteSuccess();
    void deleteFail();
    void updateFail();
    void updateSuccess();
}
