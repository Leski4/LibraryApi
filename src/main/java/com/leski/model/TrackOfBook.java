package com.leski.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "track_of_books")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrackOfBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "book_id")
    private int bookId;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
}
