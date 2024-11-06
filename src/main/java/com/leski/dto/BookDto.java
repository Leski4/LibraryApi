package com.leski.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    @Id
    @NotBlank
    @Size(min = 17, max = 17)
    private String isbn;

    @NotBlank
    @Size(min = 2, max = 150)
    private String name;

    @NotBlank
    @Size(min = 2, max = 50)
    private String genre;

    @NotBlank
    @Size(min = 2, max = 150)
    private String author;

    @Column
    @Size(max = 500)
    private String description;
}
