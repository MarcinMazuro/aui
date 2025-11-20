package org.example.lab3.controller;

import jakarta.validation.Valid;
import org.example.lab3.dto.*;
import org.example.lab3.mapper.AuthorMapper;
import org.example.lab3.mapper.BookMapper;
import org.example.lab3.model.Author;
import org.example.lab3.service.AuthorService;
import org.example.lab3.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Author resources (categories).
 * Supports hierarchical resource paths: /api/authors and /api/authors/{authorId}/books
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    public AuthorController(AuthorService authorService, BookService bookService,
                          AuthorMapper authorMapper, BookMapper bookMapper) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
    }

    /**
     * GET /api/authors - Get all authors (summary list)
     */
    @GetMapping
    public ResponseEntity<AuthorsListDto> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authorMapper.toListDto(authors));
    }

    /**
     * GET /api/authors/{id} - Get single author with details
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorReadDto> getAuthor(@PathVariable UUID id) {
        Author author = authorService.findAuthorById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        return ResponseEntity.ok(authorMapper.toReadDto(author));
    }

    /**
     * POST /api/authors - Create new author
     */
    @PostMapping
    public ResponseEntity<AuthorReadDto> createAuthor(@Valid @RequestBody AuthorCreateUpdateDto dto) {
        Author author = authorMapper.toEntity(dto);
        Author saved = authorService.saveAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorMapper.toReadDto(saved));
    }

    /**
     * PUT /api/authors/{id} - Update existing author
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthorReadDto> updateAuthor(@PathVariable UUID id,
                                                      @Valid @RequestBody AuthorCreateUpdateDto dto) {
        Author author = authorService.findAuthorById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        authorMapper.updateEntity(author, dto);
        Author saved = authorService.saveAuthor(author);
        return ResponseEntity.ok(authorMapper.toReadDto(saved));
    }

    /**
     * DELETE /api/authors/{id} - Delete author (cascades to books due to JPA configuration)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        if (!authorService.findAuthorById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/authors/{authorId}/books - Get all books by specific author
     */
    @GetMapping("/{authorId}/books")
    public ResponseEntity<BooksListDto> getBooksByAuthor(@PathVariable UUID authorId) {
        // Check if author exists - return 404 if not
        if (!authorService.findAuthorById(authorId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }

        // Return books (can be empty list if author has no books - this is OK, returns 200)
        List<org.example.lab3.model.Book> books = bookService.findBooksByAuthor(authorId);
        return ResponseEntity.ok(bookMapper.toListDto(books));
    }

    /**
     * POST /api/authors/{authorId}/books - Create a new book for specific author
     */
    @PostMapping("/{authorId}/books")
    public ResponseEntity<BookReadDto> createBook(@PathVariable UUID authorId,
                                                  @Valid @RequestBody BookCreateUpdateDto dto) {
        // Check if author exists - return 404 if trying to add book to non-existent author
        Author author = authorService.findAuthorById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        org.example.lab3.model.Book book = bookMapper.toEntity(dto);
        book.setAuthor(author);
        org.example.lab3.model.Book saved = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.toReadDto(saved));
    }
}
