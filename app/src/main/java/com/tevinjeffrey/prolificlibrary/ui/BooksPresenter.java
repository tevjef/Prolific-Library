package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.dagger.PerActivity;
import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BasePresenter;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@PerActivity
public class BooksPresenter extends BasePresenter<BooksView> {
    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public BooksPresenter(DataManager manager) {
        this.dataManager = manager;
    }

    public void loadData(boolean showLoading) {
        if (getView() != null) {
            getView().showLoading(showLoading);
        }

        RxUtils.unsubscribeIfNotNull(subscription);
        subscription = dataManager.getBooks()
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

    public void deleteAll(boolean showLoading) {
        if (getView() != null) {
            getView().showLoading(showLoading);
        }

        RxUtils.unsubscribeIfNotNull(subscription);
        subscription = dataManager.clearBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
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
                    public void onNext(Void books) {
                    }
                });
    }

}
