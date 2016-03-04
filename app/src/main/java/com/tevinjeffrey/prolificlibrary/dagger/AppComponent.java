package com.tevinjeffrey.prolificlibrary.dagger;

import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.LibraryApi;

import dagger.Component;

@Component (modules = AppModule.class)
@PerApplication
public interface AppComponent {

    DataManager dataManager();
    LibraryApi libraryApi();

    final class Initializer  {
        private Initializer() {
        }

        public static AppComponent init(LibraryApplication application) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(application))
                    .build();
        }
    }
}
