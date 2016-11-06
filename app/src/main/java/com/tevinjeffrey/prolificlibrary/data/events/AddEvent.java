package com.tevinjeffrey.prolificlibrary.data.events;

import com.tevinjeffrey.prolificlibrary.data.model.Book;

public class AddEvent extends Event {
    private final Book book;

    public AddEvent(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
