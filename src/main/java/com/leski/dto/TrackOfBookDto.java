package com.leski.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TrackOfBookDto {
    private String bookIsbn;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}