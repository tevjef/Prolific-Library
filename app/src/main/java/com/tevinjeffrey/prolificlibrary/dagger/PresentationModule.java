package com.tevinjeffrey.prolificlibrary.dagger;

import com.tevinjeffrey.prolificlibrary.dagger.annotations.PresentationScope;
import com.tevinjeffrey.prolificlibrary.dagger.annotations.RxBus;
import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.events.Event;
import com.tevinjeffrey.prolificlibrary.ui.BooksPresenter;
import com.tevinjeffrey.prolificlibrary.ui.NewBookPresenter;
import com.tevinjeffrey.prolificlibrary.ui.SingleBookPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subjects.Subject;

@Module
public class PresentationModule {

    @Provides
    @PresentationScope
    BooksPresenter providesBooksPresenter(DataManager manager, @RxBus Subject<Event, Event> rxBus) {
        return new BooksPresenter(manager, rxBus);
    }

    @Provides
    @PresentationScope
    NewBookPresenter providesNewBookPresenter(DataManager manager) {
        return new NewBookPresenter(manager);
    }

    @Provides
    @PresentationScope
    SingleBookPresenter providesSingleBookPresenter(DataManager manager) {
        return new SingleBookPresenter(manager);
    }
}
