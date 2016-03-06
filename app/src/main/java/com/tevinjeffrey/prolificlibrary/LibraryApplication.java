package com.tevinjeffrey.prolificlibrary;

import android.app.Activity;
import android.app.Application;

import com.tevinjeffrey.prolificlibrary.dagger.AppComponent;

public class LibraryApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = AppComponent.Initializer.init(this);
    }

    public static AppComponent getAppComponent(Activity activity) {
        return ((LibraryApplication)activity.getApplication()).appComponent;
    }
}
