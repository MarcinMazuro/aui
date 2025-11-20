package org.example.lab3.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a Book.
 * Does not include id or author as id is generated and author is set by the resource path.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateUpdateDto {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    @Min(1)
    private Integer pages;
    @NotNull
    @Min(1000)
    @Max(2025)
    private Integer publicationYear;
}
