package org.example.lab3.mapper;

import org.example.lab3.dto.BookCreateUpdateDto;
import org.example.lab3.dto.BookInListDto;
import org.example.lab3.dto.BookReadDto;
import org.example.lab3.dto.BooksListDto;
import org.example.lab3.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookReadDto toReadDto(Book book) {
        BookReadDto.BookReadDtoBuilder builder = BookReadDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .pages(book.getPages())
                .publicationYear(book.getPublicationYear());

        if (book.getAuthor() != null) {
            builder.author(org.example.lab3.dto.AuthorInListDto.builder()
                    .id(book.getAuthor().getId())
                    .name(book.getAuthor().getName())
                    .build());
        }

        return builder.build();
    }

    public BookInListDto toInListDto(Book book) {
        return BookInListDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .build();
    }

    public BooksListDto toListDto(List<Book> books) {
        return BooksListDto.builder()
                .books(books.stream()
                        .map(this::toInListDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Book toEntity(BookCreateUpdateDto dto) {
        return Book.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .pages(dto.getPages())
                .publicationYear(dto.getPublicationYear())
                .build();
    }

    public void updateEntity(Book book, BookCreateUpdateDto dto) {
        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setPages(dto.getPages());
        book.setPublicationYear(dto.getPublicationYear());
    }
}

