package com.tevinjeffrey.prolificlibrary.data.events;

import com.tevinjeffrey.prolificlibrary.data.model.Book;

public class UpdateEvent extends Event {
    private final Book book;
    public UpdateEvent(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
