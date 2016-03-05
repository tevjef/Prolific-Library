package com.tevinjeffrey.prolificlibrary.data;

import com.tevinjeffrey.prolificlibrary.data.model.Book;

import java.util.List;
import java.util.Map;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.PartMap;
import retrofit.http.Path;
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

    @DELETE(apiKey + "/clear/")
    Observable<Void> clearBooks();
}
