package com.tevinjeffrey.prolificlibrary.dagger;

import android.app.Activity;

import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.ui.BooksActivity;

import dagger.Component;

@Component(modules = UiModule.class, dependencies = AppComponent.class)
public interface UiComponent {
    void inject(BooksActivity booksActivity);

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
