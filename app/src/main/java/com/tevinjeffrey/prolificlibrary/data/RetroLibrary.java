package com.tevinjeffrey.prolificlibrary.data;

import com.tevinjeffrey.prolificlibrary.data.model.Book;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;

public interface RetroLibrary {
    @GET("/books")
    Observable<List<Book>> getBooks();

    @POST("/books/")
    Observable<Book> addBook(@Body String book);

    @GET("/books/{id}")
    Observable<Book> getBook(@Path("id") int bookId);

    @PUT("/books/{id}")
    Observable<Book> updateBook(@Path("id") int bookId, @Body String book);

    @DELETE("/books/{id}")
    Observable<Void> deleteBook(@Path("id") int bookId);

    @DELETE("/clear")
    Observable<Void> clearBooks();
}
