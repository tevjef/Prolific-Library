package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.dagger.PerActivity;
import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BasePresenter;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@PerActivity
public class NewBookPresenter extends BasePresenter<NewBookView> {
    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public NewBookPresenter(DataManager manager) {
        this.dataManager = manager;
    }

    public void addBook(Book newBook) {
        RxUtils.unsubscribeIfNotNull(subscription);
        subscription = dataManager.addBook(newBook)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<Book>() {
                     @Override
                     public void onCompleted() {
                         if (getView() != null) {
                             getView().showLoading(false);
                         }
                     }

                     @Override
                     public void onError(Throwable e) {
                         if (getView() != null) {
                             getView().showLoading(false);
                             getView().showError(e);
                         }
                     }

                     @Override
                    public void onNext(Book book) {
                         if (getView() != null) {
                             getView().addComplete();
                         }
                    }
                });
    }
}
