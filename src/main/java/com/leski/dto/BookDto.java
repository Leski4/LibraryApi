package com.leski.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String isbn;
    private String name;
    private String genre;
    private String author;
    private String description;
}
