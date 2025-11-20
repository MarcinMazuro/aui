package org.example.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Author implements Comparable<Author>, Serializable {
    private final String name;
    private final LocalDate birthDate;
    private final List<Book> books = new ArrayList<>();

    private Author(Builder builder) {
        if (builder.name == null || builder.name.isBlank()) {
            throw new IllegalArgumentException("Author name must not be empty");
        }
        this.name = builder.name;
        this.birthDate = builder.birthDate;
        this.books.addAll(builder.books);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public void addBook(Book book) {
        Objects.requireNonNull(book, "book");
        if (!books.contains(book)) {
            books.add(book);
            book.setAuthor(this);
        }
    }

    public boolean removeBookByTitle(String title) {
        if (title == null) return false;

        Book bookToRemove = null;
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                bookToRemove = b;
                break;
            }
        }

        if (bookToRemove != null) {
            bookToRemove.setAuthor(null);
            books.remove(bookToRemove);
            return true;
        }

        return false;
    }

    public int getAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(birthDate, now);
    }

    @Override
    public int compareTo(Author o) {
        int byName = this.name.compareToIgnoreCase(o.name);
        if (byName != 0) return byName;
        if (this.birthDate == null && o.birthDate == null) return 0;
        if (this.birthDate == null) return -1;
        if (o.birthDate == null) return 1;
        return this.birthDate.compareTo(o.birthDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return name.equalsIgnoreCase(author.name) && Objects.equals(birthDate, author.birthDate);
    }

    @Override
    public int hashCode() { return Objects.hash(name.toLowerCase(), birthDate); }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", books=" + books.size() +
                '}';
    }

    public static class Builder {
        private String name;
        private LocalDate birthDate;
        private final List<Book> books = new ArrayList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder addBook(Book book) {
            this.books.add(book);
            return this;
        }

        public Builder addBooks(List<Book> books) {
            this.books.addAll(books);
            return this;
        }

        public Author build() {
            return new Author(this);
        }
    }
}

