package com.tevinjeffrey.prolificlibrary;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.prolificlibrary.dagger.AppModule;
import com.tevinjeffrey.prolificlibrary.dagger.PresentationComponent;
import com.tevinjeffrey.prolificlibrary.dagger.annotations.PerApplication;
import com.tevinjeffrey.prolificlibrary.dagger.annotations.RxBus;
import com.tevinjeffrey.prolificlibrary.data.DataManager;
import com.tevinjeffrey.prolificlibrary.data.InMemoryDataManager;
import com.tevinjeffrey.prolificlibrary.data.RetroLibrary;
import com.tevinjeffrey.prolificlibrary.data.events.Event;

import dagger.Module;
import dagger.Provides;
import rx.subjects.Subject;

@Module(subcomponents = PresentationComponent.class)
public class FakeAppModule extends AppModule {

    private RetroLibrary retroLibrary;

    public FakeAppModule(RetroLibrary retroLibrary) {
        this.retroLibrary = retroLibrary;
    }

    @PerApplication
    public DataManager providesDataManager(RetroLibrary retroLibrary, @RxBus Subject<Event, Event> rxBus) {
        // Use use alternative implementation of DataManager to simulate user network interactions
        return new InMemoryDataManager(retroLibrary, rxBus);
    }

    @Provides
    @PerApplication
    public RetroLibrary providesRetroLibrary(OkHttpClient client, Gson gson) {
        // Use provided retroLibrary
        return retroLibrary;
    }
}
