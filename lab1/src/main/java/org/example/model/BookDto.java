package org.example.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


public class BookDto implements Comparable<BookDto>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String description;
    private final Integer pages;
    private final Integer year;
    private final String authorName;

    private BookDto(Builder b) {
        this.title = b.title;
        this.description = b.description;
        this.pages = b.pages;
        this.year = b.year;
        this.authorName = b.authorName;
    }

    public static BookDto from(Book b) {
        return new Builder()
                .title(b.getTitle())
                .description(b.getDescription())
                .pages(b.getPages())
                .year(b.getYear())
                .authorName(b.getAuthor() == null ? null : b.getAuthor().getName())
                .build();
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getPages() { return pages; }
    public Integer getYear() { return year; }
    public String getAuthorName() { return authorName; }

    @Override
    public int compareTo(BookDto o) {
        int byAuthor = str(authorName).compareToIgnoreCase(str(o.authorName));
        if (byAuthor != 0) return byAuthor;
        return str(title).compareToIgnoreCase(str(o.title));
    }

    private static String str(String s) { return s == null ? "" : s; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDto)) return false;
        BookDto that = (BookDto) o;
        return Objects.equals(title == null ? null : title.toLowerCase(), that.title == null ? null : that.title.toLowerCase()) &&
                Objects.equals(authorName == null ? null : authorName.toLowerCase(), that.authorName == null ? null : that.authorName.toLowerCase());
    }

    @Override
    public int hashCode() { return Objects.hash(str(title).toLowerCase(), str(authorName).toLowerCase()); }

    @Override
    public String toString() {
        return "BookDto{" +
                "title='" + title + '\'' +
                ", author='" + authorName + '\'' +
                ", pages=" + pages +
                ", year=" + year +
                '}';
    }

    public static class Builder {
        private String title;
        private String description;
        private Integer pages;
        private Integer year;
        private String authorName;

        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder pages(Integer pages) { this.pages = pages; return this; }
        public Builder year(Integer year) { this.year = year; return this; }
        public Builder authorName(String authorName) { this.authorName = authorName; return this; }
        public BookDto build() { return new BookDto(this); }
    }
}
