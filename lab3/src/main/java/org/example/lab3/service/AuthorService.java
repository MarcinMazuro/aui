package org.example.lab3.service;

import org.example.lab3.model.Author;
import org.example.lab3.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return repository.findAllWithBooks();
    }

    public Optional<Author> findAuthorById(UUID id) {
        return repository.findById(id);
    }

    public Author saveAuthor(Author author) {
        return repository.save(author);
    }

    public void deleteAuthor(UUID id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
