package com.tevinjeffrey.prolificlibrary.ui;

import com.tevinjeffrey.prolificlibrary.dagger.PerActivity;
import com.tevinjeffrey.prolificlibrary.dagger.RxBus;
import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.events.DeleteAllEvent;
import com.tevinjeffrey.prolificlibrary.data.events.DeleteEvent;
import com.tevinjeffrey.prolificlibrary.data.events.UpdateEvent;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BasePresenter;
import com.tevinjeffrey.prolificlibrary.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.Subject;

@PerActivity
public class BooksPresenter extends BasePresenter<BooksView> {
    private final DataManager dataManager;
    private Subject<Object, Object> rxBus;
    private Subscription subscription;
    private Subscription busSubscription;

    @Inject
    public BooksPresenter(DataManager manager, @RxBus Subject<Object, Object> rxBus) {
        this.dataManager = manager;
        this.rxBus = rxBus;
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

    @Override
    public BooksView attachView(final BooksView view) {
        busSubscription = rxBus.subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof UpdateEvent) {
                    view.updateBook(((UpdateEvent) o).getBook());
                }
                if (o instanceof DeleteEvent) {
                    view.deleteBook(((DeleteEvent) o).getId());
                }
                if (o instanceof DeleteAllEvent) {
                    view.deleteAllBooks();
                }
            }
        });
        return super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.unsubscribeIfNotNull(busSubscription);
    }
}
