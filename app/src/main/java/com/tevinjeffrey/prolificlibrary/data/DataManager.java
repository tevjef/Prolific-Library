package com.tevinjeffrey.prolificlibrary.data;

import com.tevinjeffrey.prolificlibrary.data.model.Book;

import java.util.List;

import rx.Observable;

public interface DataManager {

    Observable<List<Book>> getBooks();

    Observable<Book> addBook(Book book);

    Observable<Book> updateBook(int id, Book book);

    Observable<Void> deleteBook(int id);

    Observable<Void> clearBooks();
}
