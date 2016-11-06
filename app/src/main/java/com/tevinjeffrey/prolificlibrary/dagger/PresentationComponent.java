package com.tevinjeffrey.prolificlibrary.dagger;

import com.tevinjeffrey.prolificlibrary.dagger.annotations.PresentationScope;
import com.tevinjeffrey.prolificlibrary.ui.BooksActivity;
import com.tevinjeffrey.prolificlibrary.ui.NewBookActivity;
import com.tevinjeffrey.prolificlibrary.ui.SingleBookFragment;

import dagger.Subcomponent;

@Subcomponent(modules = PresentationModule.class)
@PresentationScope
public interface PresentationComponent {
    void inject(BooksActivity booksActivity);
    void inject(SingleBookFragment booksActivity);
    void inject(NewBookActivity newBookActivity);

    @Subcomponent.Builder
    interface Builder {
        PresentationComponent build();
        Builder with(PresentationModule module);
    }
}
