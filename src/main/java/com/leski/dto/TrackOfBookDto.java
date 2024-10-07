package com.leski.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TrackOfBookDto {
    private String isbn;
    private Boolean takenStatus;
    private LocalDateTime dateOfTake;
    private String readerName;
}