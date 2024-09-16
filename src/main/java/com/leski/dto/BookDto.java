package com.leski.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookDto {
    private String isbn;
    private String name;
    private String genre;
    private String author;
    private String description;
}
