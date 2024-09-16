package com.leski.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TrackOfBookDto {
    private int id;
    private String bookId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}