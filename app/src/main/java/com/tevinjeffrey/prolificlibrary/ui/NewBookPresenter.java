package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BasePresenter;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NewBookPresenter extends BasePresenter<NewBookView> {
    private final DataManager dataManager;
    private Subscription subscription;

    public NewBookPresenter(DataManager manager) {
        this.dataManager = manager;
    }

    public void addBook(Book newBook) {
        RxUtils.unsubscribeIfNotNull(subscription);
        subscription = dataManager.addBook(newBook)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Action1<Book>() {
                     @Override
                     public void call(Book book) {
                         if (getView() != null) {
                             getView().addComplete();
                         }
                     }
                 }, new Action1<Throwable>() {
                     @Override
                     public void call(Throwable throwable) {
                         if (getView() != null) {
                             getView().showError(throwable);
                         }
                     }
                 });
    }
}
