package org.example.lab3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for a collection of Books.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BooksListDto {
    private List<BookInListDto> books;
}

