package org.example.lab3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO for reading a single Author with all details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorReadDto {
    private UUID id;
    private String name;
    private LocalDate birthDate;
    private List<BookInListDto> books;
}

