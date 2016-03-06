package com.tevinjeffrey.prolificlibrary.dagger;

import android.app.Activity;

import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.ui.BooksActivity;
import com.tevinjeffrey.prolificlibrary.ui.NewBookActivity;
import com.tevinjeffrey.prolificlibrary.ui.SingleBookFragment;

import dagger.Component;

@Component(modules = UiModule.class, dependencies = AppComponent.class)
@PerActivity
public interface UiComponent {
    void inject(BooksActivity booksActivity);
    void inject(SingleBookFragment booksActivity);
    void inject(NewBookActivity newBookActivity);

    final class Initializer  {
        private Initializer() {
        }
        public static UiComponent init(Activity activity) {
            return DaggerUiComponent.builder()
                    .uiModule(new UiModule())
                    .appComponent(LibraryApplication.getAppComponent(activity))
                    .build();
        }
    }
}
