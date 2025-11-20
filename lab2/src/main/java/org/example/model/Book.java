package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Id
    @Builder.Default
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    @JsonIgnore
    private Author author;

    // Custom helper for CLI display including author/category info
    public String displayLine() {
        String authorName = author == null ? "<no author>" : author.getName();
        return String.format("Book{id=%s, title='%s', author='%s', pages=%s, year=%s}",
                id,
                title,
                authorName,
                pages == null ? "?" : pages,
                publicationYear == null ? "?" : publicationYear);
    }
}
