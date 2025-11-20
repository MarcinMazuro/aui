package org.example.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Book implements Comparable<Book>, Serializable {
    private final String title;
    private final String description;
    private final Integer pages;
    private final Integer year;

    // Back-ref to Author
    private Author author;

    private Book(Builder b) {
        this.title = b.title;
        this.description = b.description;
        this.pages = b.pages;
        this.year = b.year;
        this.author = b.author; // normally set by Author.addBook
        validate();
    }

    private void validate() {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Book title must not be empty");
        }
        if (pages != null && pages <= 0) {
            throw new IllegalArgumentException("Pages must be positive");
        }
        if (year != null && (year < 0 || year > 3000)) {
            throw new IllegalArgumentException("Year is out of valid range");
        }
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getPages() { return pages; }
    public Integer getYear() { return year; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    @Override
    public int compareTo(Book o) {
        int byAuthor = this.authorName().compareToIgnoreCase(o.authorName());
        if (byAuthor != 0) return byAuthor;
        int byTitle = this.title.compareToIgnoreCase(o.title);
        if (byTitle != 0) return byTitle;
        return Integer.compare(
                this.year == null ? 0 : this.year,
                o.year == null ? 0 : o.year
        );
    }

    private String authorName() {
        return author == null ? "" : author.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return authorName().equalsIgnoreCase(book.authorName()) &&
                title.equalsIgnoreCase(book.title);
    }

    @Override
    public int hashCode() { return Objects.hash(authorName().toLowerCase(), title.toLowerCase()); }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + authorName() + '\'' +
                ", pages=" + pages +
                ", year=" + year +
                '}';
    }

    public static class Builder {
        private String title;
        private String description;
        private Integer pages;
        private Integer year;
        private Author author;

        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder pages(Integer pages) { this.pages = pages; return this; }
        public Builder year(Integer year) { this.year = year; return this; }
        public Builder author(Author author) { this.author = author; return this; }
        public Book build() { return new Book(this); }
    }
}
