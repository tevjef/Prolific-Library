package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BasePresenter;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SingleBookPresenter extends BasePresenter<SingleBookView> {
    private final DataManager dataManager;
    private Subscription checkoutSubscription;
    private Subscription deleteSubscription;
    private Subscription updateSubscription;

    public SingleBookPresenter(DataManager manager) {
        this.dataManager = manager;
    }

    public void checkout(int id, Book book) {
        RxUtils.unsubscribeIfNotNull(checkoutSubscription);
        checkoutSubscription = dataManager.updateBook(id, book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Book>() {
                    @Override
                    public void call(Book book) {
                        if (getView() != null) {
                            getView().setData(book);
                            getView().checkoutSuccess();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (getView() != null) {
                            getView().checkoutFail();
                            getView().showError(throwable);
                        }
                    }
                });
    }
    
    public void update(int id, final Book book) {
        RxUtils.unsubscribeIfNotNull(updateSubscription);
        updateSubscription = dataManager.updateBook(id, book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().updateFail();
                            getView().showError(e);
                        }
                    }

                    @Override
                    public void onNext(Book book) {
                        if (getView() != null) {
                            getView().setData(book);
                            getView().updateSuccess();
                        }
                    }
                });
    }

    public void delete(int id) {
        RxUtils.unsubscribeIfNotNull(deleteSubscription);
        deleteSubscription = dataManager.deleteBook(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        if (getView() != null) {
                            getView().deleteSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().showError(e);
                            getView().deleteFail();
                        }
                    }

                    @Override
                    public void onNext(Void nil) {
                    }
                });
    }
}
