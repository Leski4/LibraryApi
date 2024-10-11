package com.leski.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackOfBookDto {
    private String isbn;
    private Boolean takenStatus;
    private LocalDateTime dateOfTake;
    private String readerName;
}