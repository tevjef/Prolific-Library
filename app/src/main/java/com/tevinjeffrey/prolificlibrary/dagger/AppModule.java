package com.tevinjeffrey.prolificlibrary.dagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.data.DateDeserializer;
import com.tevinjeffrey.prolificlibrary.data.RetroLibrary;

import java.util.Date;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import timber.log.Timber;

@Module
public class AppModule {
    private final LibraryApplication libraryApplication;

    public AppModule(LibraryApplication libraryApplication) {
        this.libraryApplication = libraryApplication;
    }

    @Provides
    @PerApplication
    public OkHttpClient providesOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.tag("OkHttp").i(message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        return client;
    }

    @Provides
    @PerApplication
    @RxBus
    public Subject<Object, Object> providesRxBus() {
        return new SerializedSubject<>(PublishSubject.create());
    }

    @Provides
    @PerApplication
    public Gson providesJson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss zzz")
                .disableHtmlEscaping()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }

    @Provides
    @PerApplication
    public RetroLibrary providesRetroLibrary(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(HttpUrl.parse("http://prolific-interview.herokuapp.com/"))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetroLibrary.class);
    }
}
