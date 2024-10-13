package com.leski.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

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
    @Column(name="taken_status")
    private Boolean takenStatus;
    @Column(name="date_of_take")
    private LocalDateTime dateOfTake;
    @Column(name="reader_name")
    private String readerName;
}