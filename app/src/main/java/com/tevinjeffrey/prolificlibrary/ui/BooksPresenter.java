package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BooksPresenter extends BasePresenter<BooksView> {
    private final DataManager dataManager;
    @Inject
    public BooksPresenter(DataManager manager) {
        this.dataManager = manager;
    }

    public void loadData(boolean showLoading) {
        if (getView() != null) {
            getView().showLoading(showLoading);
        }
        dataManager.getBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<List<Book>>() {
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
                    public void onNext(List<Book> books) {
                         if (getView() != null) {
                             getView().setData(books);
                         }
                    }
                });
    }
}
