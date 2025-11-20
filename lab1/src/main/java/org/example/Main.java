package org.example;

import org.example.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Initialize authors with books
        Author tolkien = new Author.Builder()
                .name("J.R.R. Tolkien")
                .birthDate(LocalDate.of(1892, 1, 3))
                .build();
        Author rowling = new Author.Builder()
                .name("J.K. Rowling")
                .birthDate(LocalDate.of(1965, 7, 31))
                .build();

        Book lotr = new Book.Builder()
                .title("The Lord of the Rings").description("Epic high fantasy novel")
                .pages(1178).year(1954)
                .build();
        Book hobbit = new Book.Builder()
                .title("The Hobbit").description("Children's fantasy novel")
                .pages(310).year(1937)
                .build();
        Book hp1 = new Book.Builder()
                .title("Harry Potter and the Philosopher's Stone").description("Fantasy novel")
                .pages(223).year(1997)
                .build();
        Book hp2 = new Book.Builder()
                .title("Harry Potter and the Chamber of Secrets").description("Fantasy novel")
                .pages(251).year(1998)
                .build();

        tolkien.addBook(lotr);
        tolkien.addBook(hobbit);
        rowling.addBook(hp1);
        rowling.addBook(hp2);

        Author sanderson = new Author.Builder()
                .name("Brandon Sanderson")
                .birthDate(LocalDate.of(1975, 12, 19))
                .build();

        Book wok = new Book.Builder()
                .title("The Way of Kings").description("Stormlight Archive #1")
                .pages(1007).year(2010)
                .build();
        Book wor = new Book.Builder()
                .title("Words of Radiance").description("Stormlight Archive #2")
                .pages(1087).year(2014)
                .build();
        Book oath = new Book.Builder()
                .title("Oathbringer").description("Stormlight Archive #3")
                .pages(1248).year(2017)
                .build();
        Book row = new Book.Builder()
                .title("Rhythm of War").description("Stormlight Archive #4")
                .pages(1232).year(2020)
                .build();
        Book wat = new Book.Builder()
                .title("Wind and Truth").description("Stormlight Archive #5")
                .pages(1532).year(2024)
                .build();

        sanderson.addBook(wok);
        sanderson.addBook(wor);
        sanderson.addBook(oath);
        sanderson.addBook(row);
        sanderson.addBook(wat);

        Author martin = new Author.Builder()
                .name("George R.R. Martin")
                .birthDate(LocalDate.of(1948, 9, 20))
                .build();
        Book got = new Book.Builder()
                .title("A Game of Thrones").description("A Song of Ice and Fire #1")
                .pages(694).year(1996)
                .build();
        martin.addBook(got);

        Author sapkowski = new Author.Builder()
                .name("Andrzej Sapkowski")
                .birthDate(LocalDate.of(1948, 6, 21))
                .build();
        Book tlw = new Book.Builder()
                .title("The Last Wish").description("Witcher short stories")
                .pages(288).year(1993)
                .build();
        sapkowski.addBook(tlw);

        List<Author> authors = new ArrayList<>(Arrays.asList(tolkien, rowling, sanderson, martin, sapkowski));

        System.out.println("Nested forEach lambdas:");
        authors.forEach(a -> {
            System.out.println(a);
            a.getBooks().forEach(b -> System.out.println("  " + b));
        });
        printDashes();

        Set<Book> allBooks = authors.stream()
                .flatMap(a -> a.getBooks().stream())
                .collect(Collectors.toSet());
        System.out.println("All books (Set):");
        allBooks.forEach(System.out::println);
        printDashes();

        System.out.println("Filtered (pages >= 500), sorted by title:");
        authors.stream()
                .flatMap(a -> a.getBooks().stream())
                .filter(b -> b.getPages() != null && b.getPages() >= 500)
                .sorted(Comparator.comparing(b -> b.getTitle().toLowerCase()))
                .forEach(System.out::println);
        printDashes();

        List<BookDto> dtoList = authors.stream()
                .flatMap(a -> a.getBooks().stream())
                .map(BookDto::from)
                .sorted()
                .toList();
        System.out.println("DTO List (sorted by natural order):");
        dtoList.forEach(System.out::println);
        printDashes();


        Path file = Path.of("authors.bin");
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
                oos.writeObject(authors);
            }
            List<Author> loaded;
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof List<?> list) {
                    loaded = list.stream()
                            .filter(Objects::nonNull)
                            .map(Author.class::cast)
                            .collect(Collectors.toList());
                } else {
                    loaded = Collections.emptyList();
                }
            }
            System.out.println("Loaded from file:");
            loaded.forEach(a -> {
                System.out.println(a);
                a.getBooks().forEach(b -> System.out.println("  " + b));
            });
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Serialization error: " + ex.getMessage());
        }

        printDashes();
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        printParallel(n, authors);
        printDashes();
    }

    private static void printParallel(int n, List<Author> authors) {
        System.out.println("Parallel printing with custom ForkJoinPool: ");
        ForkJoinPool pool = new ForkJoinPool(n);
        try {
            pool.submit(() -> authors.parallelStream().forEach(a -> {
                a.getBooks().forEach(b -> {
                    try {
                        System.out.printf("[%s] %s - %s%n", Thread.currentThread().getName(), a.getName(), b.getTitle());
                        TimeUnit.MILLISECONDS.sleep(150);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                });
            })).join();
        } finally {
            pool.shutdown();
        }
    }

    private static void printDashes() {
        System.out.println("-----------------------------------------------------");
    }
}