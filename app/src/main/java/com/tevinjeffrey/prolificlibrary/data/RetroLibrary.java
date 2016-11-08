package com.tevinjeffrey.prolificlibrary.data;

import com.tevinjeffrey.prolificlibrary.data.model.Book;

import java.util.List;
import java.util.Map;

import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetroLibrary {
    String apiKey = "56c4c7ecc4171e0009461b44";
    @GET(apiKey + "/books/")
    Observable<List<Book>> getBooks();

    @FormUrlEncoded
    @POST(apiKey + "/books/")
    Observable<Book> addBook(@FieldMap Map<String, String> form);

    @GET(apiKey + "/books/{id}/")
    Observable<Book> getBook(@Path("id") int bookId);

    @FormUrlEncoded
    @PUT(apiKey + "/books/{id}/")
    Observable<Book> updateBook(@Path("id") int bookId, @FieldMap Map<String, String> form);

    @DELETE(apiKey + "/books/{id}/")
    Observable<Void> deleteBook(@Path("id") int bookId);

    @DELETE(apiKey + "/clean/")
    Observable<Void> clearBooks();
}
