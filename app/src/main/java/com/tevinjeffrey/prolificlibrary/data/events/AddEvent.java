package com.tevinjeffrey.prolificlibrary.data.events;

import com.tevinjeffrey.prolificlibrary.data.model.Book;

public class AddEvent {
    private final Book book;

    public AddEvent(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
