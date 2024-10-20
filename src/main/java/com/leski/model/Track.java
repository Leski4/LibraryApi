package com.leski.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tracks")
public class Track {

    @Id
    @Size(min = 17, max = 17)
    @NotBlank
    private String bookIsbn;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    @NotNull
    private Status status;

    @Column(name="date_of_take")
    private LocalDateTime dateOfTake;

    @Column(name="reader_name")
    @Size(min = 2, max = 200)
    private String readerName;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @NotNull
    private Book book;
}
