package org.example.lab3.component;

import org.example.lab3.model.Author;
import org.example.lab3.model.Book;
import org.example.lab3.service.AuthorService;
import org.example.lab3.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
@Order(2)
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final AuthorService authorService;
    private final BookService bookService;
    private final Scanner scanner = new Scanner(System.in);

    public CommandLineRunnerImpl(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equals("cli")) {
            // Safety net: if no authors present (e.g. in tests), create one placeholder author
            if (authorService.getAllAuthors().isEmpty()) {
                Author placeholder = Author.builder().name("Placeholder Author").build();
                authorService.saveAuthor(placeholder);
            }
            listAuthors();
            runCli();
        }
    }

    private void runCli() {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1":
                    listAuthors();
                    break;
                case "2":
                    listBooks();
                    break;
                case "3":
                    addBook();
                    break;
                case "4":
                    deleteBook();
                    break;
                case "5":
                    System.exit(0);
                    break;
                case "6":
                    addAuthor();
                    break;
                case "7":
                    deleteAuthor();
                    break;
                default:
                    System.out.println("Unknown command.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nAvailable commands:");
        System.out.println("1. List all authors");
        System.out.println("2. List all books");
        System.out.println("3. Add new book");
        System.out.println("4. Delete existing book");
        System.out.println("5. Exit");
        System.out.println("6. Add new author");
        System.out.println("7. Delete existing author");
        System.out.print("Enter command: ");
    }

    private void listAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        System.out.println("\n--- Authors (" + authors.size() + ") ---");
        authors.forEach(System.out::println);
    }

    private void listBooks() {
        List<Book> books = bookService.getAllBooks();
        System.out.println("\n--- Books (" + books.size() + ") ---");
        books.forEach(b -> System.out.println(b.displayLine()));
    }

    private void addBook() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter pages: ");
        int pages = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter publication year: ");
        int publicationYear = Integer.parseInt(scanner.nextLine());

        listAuthors();
        System.out.print("Enter author ID: ");
        UUID authorId = UUID.fromString(scanner.nextLine());

        authorService.findAuthorById(authorId).ifPresent(author -> {
            Book book = Book.builder()
                    .title(title)
                    .description(description)
                    .pages(pages)
                    .publicationYear(publicationYear)
                    .author(author)
                    .build();
            bookService.saveBook(book);
            System.out.println("Book added successfully.");
        });
    }

    private void deleteBook() {
        listBooks();
        System.out.print("Enter book ID to delete: ");
        UUID bookId = UUID.fromString(scanner.nextLine());
        bookService.deleteBook(bookId);
        System.out.println("Book deleted successfully.");
    }

    private void addAuthor() {
        System.out.print("Enter author name: ");
        String name = scanner.nextLine();
        System.out.print("Enter birth date (YYYY-MM-DD) or leave empty: ");
        String bd = scanner.nextLine();
        Author.AuthorBuilder<?, ?> builder = Author.builder().name(name);
        if (!bd.isBlank()) {
            builder.birthDate(java.time.LocalDate.parse(bd));
        }
        Author author = builder.build();
        authorService.saveAuthor(author);
        System.out.println("Author added successfully: " + author.getId());
    }

    private void deleteAuthor() {
        listAuthors();
        System.out.print("Enter author ID to delete: ");
        UUID authorId = UUID.fromString(scanner.nextLine());
        authorService.deleteAuthor(authorId);
        System.out.println("Author deleted successfully.");
    }
}
