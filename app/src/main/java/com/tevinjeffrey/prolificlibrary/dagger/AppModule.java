package com.tevinjeffrey.prolificlibrary.dagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.data.RetroLibrary;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class AppModule {
    private final LibraryApplication libraryApplication;

    public AppModule(LibraryApplication libraryApplication) {
        this.libraryApplication = libraryApplication;
    }

    @Provides
    @PerApplication
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @PerApplication
    public Gson providesJson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss zzz")
                .create();
    }

    @Provides
    @PerApplication
    public RetroLibrary providesRetroLibrary(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl("http://prolific-interview.herokuapp.com/56c4c7ecc4171e0009461b44")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetroLibrary.class);
    }
}
