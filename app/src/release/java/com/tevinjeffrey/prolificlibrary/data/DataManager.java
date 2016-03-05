package com.tevinjeffrey.prolificlibrary.data;

import com.tevinjeffrey.prolificlibrary.dagger.PerApplication;
import com.tevinjeffrey.prolificlibrary.dagger.RxBus;
import com.tevinjeffrey.prolificlibrary.data.events.DeleteAllEvent;
import com.tevinjeffrey.prolificlibrary.data.events.DeleteEvent;
import com.tevinjeffrey.prolificlibrary.data.events.UpdateEvent;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.Subject;

@PerApplication
public class DataManager {
    private final RetroLibrary retroLibrary;
    private Subject<Object, Object> rxBus;
    private final int MAX_RETRIES = 2;
    private final int RETRY_DELAY = 2000;
    @Inject
    public DataManager(RetroLibrary retroLibrary, @RxBus Subject<Object, Object> rxBus) {
        this.retroLibrary = retroLibrary;
        this.rxBus = rxBus;
    }

    public Observable<List<Book>> getBooks() {
        return retroLibrary.getBooks()
                .retryWhen(new RxUtils.RetryWithDelay(MAX_RETRIES, RETRY_DELAY));
    }

    public Observable<Book> addBook(Book book) {
        return retroLibrary.addBook(book.toParamMap())
                .retryWhen(new RxUtils.RetryWithDelay(MAX_RETRIES, RETRY_DELAY));
    }

    public Observable<Book> getBook(int id) {
        return retroLibrary.getBook(id)
                .retryWhen(new RxUtils.RetryWithDelay(MAX_RETRIES, RETRY_DELAY));
    }

    public Observable<Book> updateBook(int id, Book book) {
        return retroLibrary.updateBook(id, book.toParamMap())
                .retryWhen(new RxUtils.RetryWithDelay(MAX_RETRIES, RETRY_DELAY)).doOnNext(new Action1<Book>() {
            @Override
            public void call(Book book) {
                RxUtils.RxBus.send(rxBus, new UpdateEvent(book));
            }
        });
    }

    public Observable<Void> deleteBook(final int id) {
        return retroLibrary.deleteBook(id)
                .retryWhen(new RxUtils.RetryWithDelay(MAX_RETRIES, RETRY_DELAY))
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        RxUtils.RxBus.send(rxBus, new DeleteEvent(id));
                    }
                });
    }

    public Observable<Void> clearBooks() {
        return retroLibrary.clearBooks()
                .retryWhen(new RxUtils.RetryWithDelay(MAX_RETRIES, RETRY_DELAY))
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        RxUtils.RxBus.send(rxBus, new DeleteAllEvent());
                    }
                });
    }
}
