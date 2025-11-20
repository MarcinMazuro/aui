package org.example.lab3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for reading a single Book with all details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReadDto {
    private UUID id;
    private String title;
    private String description;
    private Integer pages;
    private Integer publicationYear;
    private AuthorInListDto author;
}

