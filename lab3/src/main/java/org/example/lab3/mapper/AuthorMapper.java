package org.example.lab3.mapper;

import org.example.lab3.dto.AuthorCreateUpdateDto;
import org.example.lab3.dto.AuthorInListDto;
import org.example.lab3.dto.AuthorReadDto;
import org.example.lab3.dto.AuthorsListDto;
import org.example.lab3.model.Author;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    private final BookMapper bookMapper;

    public AuthorMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public AuthorReadDto toReadDto(Author author) {
        return AuthorReadDto.builder()
                .id(author.getId())
                .name(author.getName())
                .birthDate(author.getBirthDate())
                .books(author.getBooks().stream()
                        .map(bookMapper::toInListDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public AuthorInListDto toInListDto(Author author) {
        return AuthorInListDto.builder()
                .id(author.getId())
                .name(author.getName())
                .build();
    }

    public AuthorsListDto toListDto(List<Author> authors) {
        return AuthorsListDto.builder()
                .authors(authors.stream()
                        .map(this::toInListDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Author toEntity(AuthorCreateUpdateDto dto) {
        return Author.builder()
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .build();
    }

    public void updateEntity(Author author, AuthorCreateUpdateDto dto) {
        author.setName(dto.getName());
        author.setBirthDate(dto.getBirthDate());
    }
}

