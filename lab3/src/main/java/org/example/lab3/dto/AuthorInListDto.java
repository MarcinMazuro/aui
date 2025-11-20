package org.example.lab3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for Author in collection - contains only id and name.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorInListDto {
    private UUID id;
    private String name;
}

