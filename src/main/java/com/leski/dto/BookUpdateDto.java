package com.leski.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookUpdateDto {
    private String name;
    private String genre;
    private String author;
    private String description;
}
