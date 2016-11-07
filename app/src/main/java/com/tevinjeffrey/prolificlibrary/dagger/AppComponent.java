package com.tevinjeffrey.prolificlibrary.dagger;

import com.tevinjeffrey.prolificlibrary.dagger.annotations.PerApplication;

import dagger.Component;

@Component (modules = AppModule.class)
@PerApplication
public interface AppComponent {
    PresentationComponent.Builder presentationComponentBuilder();
}
