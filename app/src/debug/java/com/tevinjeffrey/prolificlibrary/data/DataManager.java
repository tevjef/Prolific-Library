package com.tevinjeffrey.prolificlibrary.data;

import com.tevinjeffrey.prolificlibrary.dagger.PerApplication;
import com.tevinjeffrey.prolificlibrary.dagger.RxBus;
import com.tevinjeffrey.prolificlibrary.data.events.DeleteAllEvent;
import com.tevinjeffrey.prolificlibrary.data.events.DeleteEvent;
import com.tevinjeffrey.prolificlibrary.data.events.UpdateEvent;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.Subject;

@PerApplication
public class DataManager {
    private final RetroLibrary retroLibrary;
    private Subject<Object, Object> rxBus;

    private List<Book> cache;

    @Inject
    public DataManager(RetroLibrary retroLibrary, @RxBus Subject<Object, Object> rxBus) {
        this.retroLibrary = retroLibrary;
        this.rxBus = rxBus;
    }

    public Observable<List<Book>> getBooks() {
        return retroLibrary.getBooks().flatMap(new Func1<List<Book>, Observable<List<Book>>>() {
            @Override
            public Observable<List<Book>> call(List<Book> books) {
                if (cache == null) {
                    cache = books;
                }
                return Observable.just(cache);
            }
        });
    }

    public Observable<Book> addBook(Book book) {
        int id = (int)(Math.random() * 1000 + 100);
        book.setId(id);
        cache.add(book);
        return Observable.just(book);
    }

    public Observable<Book> getBook(int id) {
        int index = cache.indexOf(new Book.Builder().id(id).build());
        return Observable.just(cache.get(index));
    }

    public Observable<Book> updateBook(int id, Book book) {
        int index = cache.indexOf(new Book.Builder().id(id).build());
        Book inCache = cache.get(index);

        Book.Builder bookBuilder = new Book.Builder()
                .id(id)
                .url(inCache.getUrl());

        // If the field is null in the book parameter, it's not being updated, so use the value
        // from the inCache to create the updated book
        bookBuilder = book.getTitle() == null? bookBuilder.title(inCache.getTitle()) : bookBuilder.title(book.getTitle());
        bookBuilder = book.getAuthor() == null? bookBuilder.author(inCache.getAuthor()) : bookBuilder.author(book.getAuthor());
        bookBuilder = book.getPublisher() == null? bookBuilder.publisher(inCache.getPublisher()) : bookBuilder.publisher(book.getPublisher());
        if (book.getLastCheckedOutBy() == null) {
            bookBuilder = bookBuilder.lastCheckedOutBy(inCache.getLastCheckedOutBy());
            bookBuilder = bookBuilder.lastCheckOut(inCache.getLastCheckedOut());
        } else {
            bookBuilder.lastCheckedOutBy(book.getLastCheckedOutBy());
            bookBuilder.lastCheckOut(new Date(System.currentTimeMillis() - 1000));
        }
        bookBuilder = book.getCategories() == null? bookBuilder.categories(inCache.getCategories()) : bookBuilder.categories(book.getCategories());

        // Replace cache version with new version
        cache.set(index, bookBuilder.build());

        return Observable.just(cache.get(index))
                .doOnNext(new Action1<Book>() {
                    @Override
                    public void call(Book book) {
                        RxUtils.RxBus.send(rxBus, new UpdateEvent(book));
                    }
                });
    }

    public Observable<Void> deleteBook(final int id) {
        int index = cache.indexOf(new Book.Builder().id(id).build());
        cache.get(index);
        return Observable.<Void>empty().doOnCompleted(new Action0() {
            @Override
            public void call() {
                RxUtils.RxBus.send(rxBus, new DeleteEvent(id));
            }
        });
    }

    public Observable<Void> clearBooks() {
        cache.clear();
        return Observable.<Void>empty().doOnCompleted(new Action0() {
            @Override
            public void call() {
                RxUtils.RxBus.send(rxBus, new DeleteAllEvent());
            }
        });
    }
}
