package com.leski.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

@Entity
@Table(name = "books")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private String isbn;
    @Column
    private String name;
    @Column
    private String genre;
    @Column
    private String author;
    @Column
    private String description;
}