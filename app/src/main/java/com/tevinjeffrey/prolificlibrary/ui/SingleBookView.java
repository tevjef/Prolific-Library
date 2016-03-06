package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BaseView;

interface SingleBookView extends BaseView {
    void showError(Throwable e);
    void setData(Book updatedBook);
    void checkoutSuccess();
    void checkoutFail();
    void deleteSuccess();
    void deleteFail();
    void updateSuccess();
    void updateFail();
}
