package org.example.lab3.controller;

import jakarta.validation.Valid;
import org.example.lab3.dto.BookCreateUpdateDto;
import org.example.lab3.dto.BookReadDto;
import org.example.lab3.dto.BooksListDto;
import org.example.lab3.mapper.BookMapper;
import org.example.lab3.model.Book;
import org.example.lab3.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Book resources (elements).
 * Supports: /api/books for all books
 * Books can also be accessed via /api/authors/{authorId}/books
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    /**
     * GET /api/books - Get all books (summary list)
     */
    @GetMapping
    public ResponseEntity<BooksListDto> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(bookMapper.toListDto(books));
    }

    /**
     * GET /api/books/{id} - Get single book with details
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookReadDto> getBook(@PathVariable UUID id) {
        Book book = bookService.findBookById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        return ResponseEntity.ok(bookMapper.toReadDto(book));
    }

    /**
     * PUT /api/books/{id} - Update existing book
     * Note: This updates book fields but NOT the author relationship
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookReadDto> updateBook(@PathVariable UUID id,
                                                  @Valid @RequestBody BookCreateUpdateDto dto) {
        Book book = bookService.findBookById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        bookMapper.updateEntity(book, dto);
        Book saved = bookService.saveBook(book);
        return ResponseEntity.ok(bookMapper.toReadDto(saved));
    }

    /**
     * DELETE /api/books/{id} - Delete book
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        if (!bookService.findBookById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
