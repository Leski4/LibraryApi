package com.leski.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @NotBlank
    @Size(min = 17, max = 17)
    private String isbn;

    @Column
    @NotBlank
    @Size(min = 2, max = 150)
    private String name;

    @Column
    @NotBlank
    @Size(min = 2, max = 50)
    private String genre;

    @Column
    @NotBlank
    @Size(min = 2, max = 150)
    private String author;

    @Column
    @Size(max = 500)
    private String description;
}