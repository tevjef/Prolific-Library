package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BaseView;

import java.util.List;

public interface BooksView extends BaseView {
    void showError(Throwable e);
    void setData(List<Book> books);
    void showLoading(boolean isLoading);
    void updateBook(Book updatedBook);
    void deleteBook(int id);
    void deleteAllBooks();
}
