package com.tevinjeffrey.prolificlibrary.dagger;

import com.tevinjeffrey.prolificlibrary.BuildConfig;
import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.data.DataManager;

import javax.inject.Named;

import dagger.Component;
import rx.subjects.Subject;

@Component (modules = AppModule.class)
@PerApplication
public interface AppComponent {

    DataManager dataManager();
    @RxBus Subject<Object, Object> rxBus();

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
