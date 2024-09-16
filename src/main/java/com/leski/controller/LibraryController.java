package com.leski.controller;

import com.leski.dto.TrackOfBookDto;
import com.leski.model.TrackOfBook;
import com.leski.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Track;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/library")
@Tag(name = "Учёт библиотечных книг")
public class LibraryController {
    private final LibraryService libraryService;
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public LibraryController(LibraryService libraryService){
        this.libraryService = libraryService;
    }
    @GetMapping("/getFreeBooks")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение списка свободных книг")
    public ResponseEntity<List<TrackOfBookDto>> getAllFreeBooks(){
        List<TrackOfBook> trackOfBooks = libraryService.getAllTrackOfBooks();
        return ResponseEntity.ok(trackOfBooks.stream()
                .map(trackOfBook -> modelMapper.map(trackOfBook,TrackOfBookDto.class))
                .filter(trackOfBook -> trackOfBook.getStartDate() == null)
                .toList());
    }
    @GetMapping("/getTrackOfBooks")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение всех книг")
    public ResponseEntity<List<TrackOfBookDto>> getAllBooks(){
        List<TrackOfBook> trackOfBooks = libraryService.getAllTrackOfBooks();
        return ResponseEntity.ok(trackOfBooks.stream()
                .map(trackOfBook -> modelMapper.map(trackOfBook,TrackOfBookDto.class))
                .toList());
    }
    @GetMapping("/takeBookById/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Взятие книги")
    public ResponseEntity<TrackOfBookDto> takeBook(@PathVariable int id){
        TrackOfBook trackOfBook = libraryService.getTrackByBookId(id);
        trackOfBook.setStartDate(LocalDateTime.now());
        trackOfBook.setEndDate(LocalDateTime.now().plusMonths(1));
        libraryService.addTrack(trackOfBook);
        TrackOfBookDto trackOfBookDto = modelMapper.map(trackOfBook,TrackOfBookDto.class);
        return ResponseEntity.ok(trackOfBookDto);
    }
}