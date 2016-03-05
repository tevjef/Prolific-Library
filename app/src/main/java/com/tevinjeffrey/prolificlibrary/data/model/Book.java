package com.tevinjeffrey.prolificlibrary.data.model;

import android.os.Parcelable;
import android.text.format.DateUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Book implements Parcelable {
    private int id;
    private String author;
    private String categories;
    private Date lastCheckedOut;
    private String lastCheckedOutBy;
    private String publisher;
    private String title;
    private String url;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategories() {
        return categories;
    }

    public String getLastCheckedOutString() {
        if (lastCheckedOut == null) return null;
        return DateUtils.getRelativeTimeSpanString(lastCheckedOut.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

    public Date getLastCheckedOut() {
        return lastCheckedOut;
    }

    public String getLastCheckedOutBy() {
        return lastCheckedOutBy;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> toParamMap() {
        Map<String, String> map = new LinkedHashMap<>();
        if (author != null) map.put("author", author);
        if (categories != null) map.put("categories", categories);
        if (lastCheckedOutBy != null) map.put("lastCheckedOutBy", lastCheckedOutBy);
        if (publisher != null) map.put("publisher", publisher);
        if (title != null) map.put("title", title);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return id == book.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", categories='" + categories + '\'' +
                ", lastCheckedOut='" + lastCheckedOut + '\'' +
                ", lastCheckedOutBy='" + lastCheckedOutBy + '\'' +
                ", publisher='" + publisher + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    // Kotlin please save us!
    public static class Builder {
        Book book;
        public Builder() {
            book = new Book();
        }

        public Builder id(int id) {
            book.id = id;
            return this;
        }


        public Builder author(String author) {
            book.author = author;
            return this;
        }

        public Builder categories(String categories) {
            book.categories = categories;
            return this;
        }

        public Builder lastCheckedOutBy(String lastCheckedOutBy) {
            book.lastCheckedOutBy = lastCheckedOutBy;
            return this;
        }

        public Builder lastCheckOut(Date lastCheckedOut) {
            book.lastCheckedOut = lastCheckedOut;
            return this;
        }

        public Builder publisher(String publisher) {
            book.publisher = publisher;
            return this;
        }

        public Builder title(String title) {
            book.title = title;
            return this;
        }

        public Builder url(String url) {
            book.url = url;
            return this;
        }

        public Book build() {
            return book;
        }

    }

    public Book() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.author);
        dest.writeString(this.categories);
        dest.writeLong(lastCheckedOut != null ? lastCheckedOut.getTime() : -1);
        dest.writeString(this.lastCheckedOutBy);
        dest.writeString(this.publisher);
        dest.writeString(this.title);
        dest.writeString(this.url);
    }

    protected Book(android.os.Parcel in) {
        this.id = in.readInt();
        this.author = in.readString();
        this.categories = in.readString();
        long tmpLastCheckedOut = in.readLong();
        this.lastCheckedOut = tmpLastCheckedOut == -1 ? null : new Date(tmpLastCheckedOut);
        this.lastCheckedOutBy = in.readString();
        this.publisher = in.readString();
        this.title = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(android.os.Parcel source) {
            return new Book(source);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
