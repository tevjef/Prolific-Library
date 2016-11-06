package com.tevinjeffrey.prolificlibrary;

import android.app.Activity;
import android.app.Application;

import com.tevinjeffrey.prolificlibrary.dagger.AppComponent;
import com.tevinjeffrey.prolificlibrary.dagger.DaggerAppComponent;
import com.tevinjeffrey.prolificlibrary.dagger.PresentationComponent;

public class LibraryApplication extends Application {

    private PresentationComponent presentationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        AppComponent appComponent = DaggerAppComponent.builder().build();

        presentationComponent = appComponent.presentationComponentBuilder().build();
    }

    public static PresentationComponent presentationComponent(Activity activity) {
        return ((LibraryApplication)activity.getApplication()).presentationComponent;
    }
}
