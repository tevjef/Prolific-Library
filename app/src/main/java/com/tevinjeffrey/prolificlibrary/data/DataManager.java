package com.tevinjeffrey.prolificlibrary.data;

import com.tevinjeffrey.prolificlibrary.dagger.PerApplication;
import com.tevinjeffrey.prolificlibrary.data.model.Book;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

@PerApplication
public class DataManager {
    private final RetroLibrary retroLibrary;

    @Inject
    public DataManager(RetroLibrary retroLibrary) {
        this.retroLibrary = retroLibrary;
    }

    public Observable<List<Book>> getBooks() {
        return retroLibrary.getBooks();
    }

    public Observable<Book> addBook(Book book) {
        return retroLibrary.addBook(book.toParamString());
    }

    public Observable<Book> getBook(int id) {
        return retroLibrary.getBook(id);
    }

    public Observable<Book> updateBook(int id, Book book) {
        return retroLibrary.updateBook(id, book.toParamString());
    }

    public Observable<Void> deletebook(int id) {
        return retroLibrary.deleteBook(id);
    }

    public Observable<Void> clearBooks() {
        return retroLibrary.clearBooks();
    }
}
