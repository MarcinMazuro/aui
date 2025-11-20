package org.example.lab3.service;

import org.example.lab3.model.Book;
import org.example.lab3.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return repository.findAllWithAuthor();
    }

    @Transactional(readOnly = true)
    public Optional<Book> findBookById(UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Book> findBooksByAuthor(UUID authorId) {
        return repository.findByAuthor_Id(authorId);
    }

    public Book saveBook(Book book) {
        return repository.save(book);
    }

    public void deleteBook(UUID id) {
        repository.deleteById(id);
    }
}
