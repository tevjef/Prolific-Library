package com.tevinjeffrey.prolificlibrary.data.events;

public class DeleteEvent {
    private final int id;

    public DeleteEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
