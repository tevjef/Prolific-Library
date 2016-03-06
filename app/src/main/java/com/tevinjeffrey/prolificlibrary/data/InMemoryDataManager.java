package com.tevinjeffrey.prolificlibrary.data;

import com.tevinjeffrey.prolificlibrary.dagger.annotations.RxBus;
import com.tevinjeffrey.prolificlibrary.data.events.AddEvent;
import com.tevinjeffrey.prolificlibrary.data.events.DeleteAllEvent;
import com.tevinjeffrey.prolificlibrary.data.events.DeleteEvent;
import com.tevinjeffrey.prolificlibrary.data.events.UpdateEvent;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.Subject;

/**
 * Replicates the behavior of the server's CRUD operations within memory
 * */
public class InMemoryDataManager implements DataManager {
    private final RetroLibrary retroLibrary;
    private final Subject<Object, Object> rxBus;

    private List<Book> cache;

    public InMemoryDataManager(RetroLibrary retroLibrary, @RxBus Subject<Object, Object> rxBus) {
        this.retroLibrary = retroLibrary;
        this.rxBus = rxBus;
    }

    @Override
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

    @Override
    public Observable<Book> addBook(Book book) {
        int id = (int)(Math.random() * 1000 + 100);
        book.setId(id);
        cache.add(book);
        return Observable.just(book)
                .doOnNext(new Action1<Book>() {
                    @Override
                    public void call(Book book) {
                        RxUtils.RxBus.send(rxBus, new AddEvent(book));
                    }
                });
    }

    @Override
    public Observable<Book> updateBook(int id, Book book) {
        int index = cache.indexOf(new Book.Builder().id(id).build());
        Book cacheBook = cache.get(index);

        Book.Builder bookBuilder = new Book.Builder()
                .id(id)
                .url(cacheBook.getUrl());

        // If the field is null in the book parameter, it's not being updated, so use the value
        // from the cacheBook to create the updated book
        if (book.getTitle() == null) {
            bookBuilder.title(cacheBook.getTitle());
        } else {
            bookBuilder.title(book.getTitle());
        }
        if (book.getAuthor() == null) {
            bookBuilder.author(cacheBook.getAuthor());
        } else {
            bookBuilder.author(book.getAuthor());
        }
        if (book.getPublisher() == null) {
            bookBuilder.publisher(cacheBook.getPublisher());
        } else {
            bookBuilder.publisher(book.getPublisher());
        }
        if (book.getLastCheckedOutBy() == null) {
            bookBuilder = bookBuilder.lastCheckedOutBy(cacheBook.getLastCheckedOutBy());
            bookBuilder = bookBuilder.lastCheckOut(cacheBook.getLastCheckedOut());
        } else {
            bookBuilder.lastCheckedOutBy(book.getLastCheckedOutBy());
            // Timestamp update to a 1 second ago.
            bookBuilder.lastCheckOut(new Date(System.currentTimeMillis() - 1000));
        }
        if (book.getCategories() == null) {
            bookBuilder.categories(cacheBook.getCategories());
        } else {
            bookBuilder.categories(book.getCategories());
        }

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

    @Override
    public Observable<Void> deleteBook(final int id) {
        int index = cache.indexOf(new Book.Builder().id(id).build());
        cache.remove(index);
        return Observable.<Void>empty().doOnCompleted(new Action0() {
            @Override
            public void call() {
                RxUtils.RxBus.send(rxBus, new DeleteEvent(id));
            }
        });
    }

    @Override
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