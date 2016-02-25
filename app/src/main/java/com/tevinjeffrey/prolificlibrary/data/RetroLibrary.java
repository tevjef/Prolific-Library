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
    String apiKey = "56c4c7ecc4171e0009461b44";
    @GET(apiKey + "/books/")
    Observable<List<Book>> getBooks();

    @POST(apiKey + "/books/")
    Observable<Book> addBook(@Body String book);

    @GET(apiKey + "/books/{id}/")
    Observable<Book> getBook(@Path("id") int bookId);

    @PUT(apiKey + "/books/{id}/")
    Observable<Book> updateBook(@Path("id") int bookId, @Body String book);

    @DELETE(apiKey + "/books/{id}/")
    Observable<Void> deleteBook(@Path("id") int bookId);

    @DELETE(apiKey + "/clear/")
    Observable<Void> clearBooks();
}
