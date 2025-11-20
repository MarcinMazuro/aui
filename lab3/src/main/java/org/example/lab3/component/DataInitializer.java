package org.example.lab3.component;

import org.example.lab3.model.Author;
import org.example.lab3.model.Book;
import org.example.lab3.service.AuthorService;
import org.example.lab3.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AuthorService authorService;
    private final BookService bookService;

    public DataInitializer(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!authorService.getAllAuthors().isEmpty()) {
            log.info("Skipping seeding - authors already present: {}", authorService.getAllAuthors().size());
            return;
        }
        log.info("Seeding example authors and books");

        Author tolkien = Author.builder()
                .name("J.R.R. Tolkien")
                .birthDate(LocalDate.of(1892, 1, 3))
                .build();
        authorService.saveAuthor(tolkien);

        Book lotr = Book.builder().title("The Lord of the Rings").description("Epic high fantasy novel").pages(1178).publicationYear(1954).author(tolkien).build();
        bookService.saveBook(lotr);

        Author orwell = Author.builder()
                .name("George Orwell")
                .birthDate(LocalDate.of(1903, 6, 25))
                .build();
        authorService.saveAuthor(orwell);

        Book nineteenEightyFour = Book.builder().title("1984").description("Dystopian social science fiction novel").pages(328).publicationYear(1949).author(orwell).build();
        Book animalFarm = Book.builder().title("Animal Farm").description("Allegorical novella").pages(112).publicationYear(1945).author(orwell).build();
        bookService.saveBook(nineteenEightyFour);
        bookService.saveBook(animalFarm);

        Author asimov = Author.builder()
                .name("Isaac Asimov")
                .birthDate(LocalDate.of(1920, 1, 2))
                .build();
        authorService.saveAuthor(asimov);

        Book foundation = Book.builder().title("Foundation").description("Science fiction novel").pages(255).publicationYear(1951).author(asimov).build();
        bookService.saveBook(foundation);

        log.info("Seeding finished. Authors in DB: {}", authorService.getAllAuthors().size());
    }
}
