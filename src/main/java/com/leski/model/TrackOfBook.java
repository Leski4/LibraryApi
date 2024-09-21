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
    private String bookIsbn;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
}
