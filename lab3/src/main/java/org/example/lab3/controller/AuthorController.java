package org.example.lab3.controller;

import org.example.lab3.model.Author;
import org.example.lab3.model.Book;
import org.example.lab3.service.AuthorService;
import org.example.lab3.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public List<Author> allAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}/books")
    public List<Book> booksByAuthor(@PathVariable UUID id) {
        return bookService.findBooksByAuthor(id);
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author saved = authorService.saveAuthor(author);
        return ResponseEntity.status(201).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
