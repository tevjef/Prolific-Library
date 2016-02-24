package com.tevinjeffrey.prolificlibrary;

import android.app.Activity;
import android.app.Application;

import com.tevinjeffrey.prolificlibrary.dagger.AppComponent;

import timber.log.Timber;

public class LibraryApplication extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = AppComponent.Initializer.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static AppComponent getAppComponent(Activity activity) {
        return ((LibraryApplication)activity.getApplication()).appComponent;
    }
}
