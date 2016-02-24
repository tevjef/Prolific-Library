package com.tevinjeffrey.prolificlibrary.data.model;

import org.parceler.Parcel;

@Parcel
public class Book {
    private int id;
    private String author;
    private String categories;
    private String lastCheckedOut;
    private String lastCheckedOutBy;
    private String publisher;
    private String title;
    private String url;

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
}
