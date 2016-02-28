package com.tevinjeffrey.prolificlibrary.data.model;

import android.os.Parcelable;

import org.parceler.Parcel;

public class Book implements Parcelable {
    int id;
    String author;
    String categories;
    String lastCheckedOut;
    String lastCheckedOutBy;
    String publisher;
    String title;
    String url;

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategories() {
        return categories;
    }

    public String getLastCheckedOut() {
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

    public String toParamString() {
        StringBuilder sb = new StringBuilder();
        if (author != null) sb.append("author=").append(author);
        if (categories != null) sb.append("categories=").append(categories);
        if (lastCheckedOutBy != null) sb.append("lastCheckedOutBy=").append(lastCheckedOutBy);
        if (publisher != null) sb.append("publisher=").append(publisher);
        if (title != null) sb.append("title=").append(title);
        return sb.toString();
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

        public Builder publisher(String publisher) {
            book.publisher = publisher;
            return this;
        }

        public Builder title(String title) {
            book.title = title;
            return this;
        }

        public Book build() {
            return book;
        }

        private Builder lastCheckedOut(String lastCheckedOut) {
            book.lastCheckedOut = lastCheckedOut;
            return this;
        }

        private Builder url(String url) {
            book.url = url;
            return this;
        }
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
        dest.writeString(this.lastCheckedOut);
        dest.writeString(this.lastCheckedOutBy);
        dest.writeString(this.publisher);
        dest.writeString(this.title);
        dest.writeString(this.url);
    }

    public Book() {
    }

    protected Book(android.os.Parcel in) {
        this.id = in.readInt();
        this.author = in.readString();
        this.categories = in.readString();
        this.lastCheckedOut = in.readString();
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
