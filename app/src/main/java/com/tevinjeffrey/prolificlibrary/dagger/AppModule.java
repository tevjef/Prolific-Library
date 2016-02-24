package com.tevinjeffrey.prolificlibrary.dagger;

import com.tevinjeffrey.prolificlibrary.LibraryApplication;

import dagger.Module;

@Module
public class AppModule {
    private final LibraryApplication libraryApplication;

    public AppModule(LibraryApplication libraryApplication) {
        this.libraryApplication = libraryApplication;
    }
}
