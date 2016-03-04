package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BasePresenter;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SingleBookPresenter extends BasePresenter<SingleBookView> {
    private final DataManager dataManager;
    private Subscription checkoutSubscription;
    private Subscription deleteSubscription;

    @Inject
    public SingleBookPresenter(DataManager manager) {
        this.dataManager = manager;
    }

    public void checkout(int id, Book book) {
        RxUtils.unsubscribeIfNotNull(checkoutSubscription);
        checkoutSubscription = dataManager.updateBook(id, book)
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
                            getView().checkoutFail();
                            getView().showError(e);
                        }
                    }

                    @Override
                    public void onNext(Book books) {
                        if (getView() != null) {
                            getView().checkoutSuccess();
                        }
                    }
                });
    }

    public void delete(int id) {
        RxUtils.unsubscribeIfNotNull(deleteSubscription);
        deleteSubscription = dataManager.deletebook(id)
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
                            getView().deleteFail();
                            getView().showError(e);
                        }
                    }

                    @Override
                    public void onNext(Void books) {
                        if (getView() != null) {
                            getView().deleteSuccess();
                        }
                    }
                });
    }
}
