package com.tevinjeffrey.prolificlibrary;

import android.app.Activity;
import android.app.Application;

import com.tevinjeffrey.prolificlibrary.dagger.AppComponent;
import com.tevinjeffrey.prolificlibrary.dagger.DaggerAppComponent;
import com.tevinjeffrey.prolificlibrary.dagger.PresentationComponent;

public class LibraryApplication extends Application {

    private PresentationComponent presentationComponent;
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setComponents(DaggerAppComponent.builder().build());

    }

    public void setComponents(AppComponent appComponent) {
        this.appComponent = appComponent;
        presentationComponent = appComponent.presentationComponentBuilder().build();
    }

    public static PresentationComponent presentationComponent(Activity activity) {
        return ((LibraryApplication)activity.getApplication()).presentationComponent;
    }
}
