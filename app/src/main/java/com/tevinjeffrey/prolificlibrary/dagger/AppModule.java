package com.tevinjeffrey.prolificlibrary.dagger;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tevinjeffrey.prolificlibrary.dagger.annotations.PerApplication;
import com.tevinjeffrey.prolificlibrary.dagger.annotations.RxBus;
import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.DataManagerImpl;
import com.tevinjeffrey.prolificlibrary.data.DateDeserializer;
import com.tevinjeffrey.prolificlibrary.data.RetroLibrary;
import com.tevinjeffrey.prolificlibrary.data.events.Event;

import java.util.Date;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Module(subcomponents = PresentationComponent.class)
public class AppModule {
    @Provides
    @PerApplication
    public OkHttpClient providesOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("OkHttp", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(interceptor);
        return builder.build();
    }

    @Provides
    @PerApplication
    public DataManager providesDataManager(RetroLibrary retroLibrary, @RxBus Subject<Event, Event> rxBus) {
        return new DataManagerImpl(retroLibrary, rxBus);
    }

    @Provides
    @PerApplication
    @RxBus
    public Subject<Event, Event> providesRxBus() {
        return new SerializedSubject<>(PublishSubject.<Event>create());
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
