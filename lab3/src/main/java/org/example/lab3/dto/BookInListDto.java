package org.example.lab3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for Book in collection - contains only id and title.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookInListDto {
    private UUID id;
    private String title;
}

