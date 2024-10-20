package com.leski.dto;

import com.leski.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackOfBookDto {

    @Size(min = 17, max = 17)
    @NotBlank
    private String isbn;

    @NotNull
    private Status status;


    private LocalDateTime dateOfTake;

    @Size(min = 2, max = 200)
    private String readerName;
}