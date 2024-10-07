package com.leski.controller;

import com.leski.dto.TrackOfBookDto;
import com.leski.model.Book;
import com.leski.service.LibraryService;
import com.leski.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/library")
@Tag(name = "Учёт библиотечных книг")
public class LibraryController {
    private final LibraryService libraryService;
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public LibraryController(LibraryService libraryService, UserService userService){
        this.libraryService = libraryService;
        this.userService = userService;
    }
    @GetMapping("/getFreeBooks")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение списка свободных книг")
    public ResponseEntity<List<TrackOfBookDto>> getAllFreeBooks(){
        List<TrackOfBookDto> trackOfBooks = libraryService.getAllTrackOfBooks();

        return ResponseEntity.ok(trackOfBooks.stream()
                .filter(trackOfBookDto -> !trackOfBookDto.getTakenStatus())
                .toList());
    }
    @GetMapping("/admin/getTrackOfBooks")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Учёт книг")
    public ResponseEntity<List<TrackOfBookDto>> getAllBooks(){
        List<TrackOfBookDto> trackOfBooks = libraryService.getAllTrackOfBooks();
        return ResponseEntity.ok(trackOfBooks);
    }
    @PutMapping("/takeBookById/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Взять книгу")
    public ResponseEntity<TrackOfBookDto> takeBook(@PathVariable String id) throws Exception {
        Optional<TrackOfBookDto> trackOfBook = libraryService.getTrackByBookIsbn(id);
        if(trackOfBook.isEmpty())
            throw new Exception();
        trackOfBook.get().setDateOfTake(LocalDateTime.now());
        trackOfBook.get().setTakenStatus(true);
        trackOfBook.get().setReaderName(SecurityContextHolder.getContext().getAuthentication().getName());
        //trackOfBook.setEndDate(LocalDateTime.now().plusMonths(1));
        TrackOfBookDto trackOfBookDto = libraryService.makeTrack(trackOfBook.get());
        return ResponseEntity.ok(trackOfBookDto);
    }
    @PutMapping("/returnBookById/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Вернуть книгу")
    public ResponseEntity<TrackOfBookDto> returnBook(@PathVariable String id){
        Optional<TrackOfBookDto> trackOfBook = libraryService.getTrackByBookIsbn(id);
        if(trackOfBook.isEmpty())
            return null;
        if(!Objects.equals(trackOfBook.get().getReaderName(),
                SecurityContextHolder.getContext().getAuthentication().getName())) {
            return null;
        }
        trackOfBook.get().setDateOfTake(null);
        trackOfBook.get().setReaderName(null);
        trackOfBook.get().setTakenStatus(false);
        TrackOfBookDto trackOfBookDto = libraryService.deleteTrack(trackOfBook.get());
        return ResponseEntity.ok(trackOfBookDto);
    }
    @PutMapping("/admin/makeTrack/{username}/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Выдать книгу читателю")
    public ResponseEntity.BodyBuilder makeTrack(@PathVariable String username, @PathVariable String id){
        if(!userService.existsUser(username)){
            return ResponseEntity.badRequest();
        }
        Optional<TrackOfBookDto> trackOfBook = libraryService.getTrackByBookIsbn(id);
        if(trackOfBook.isEmpty()){
            return ResponseEntity.badRequest();
        }
        TrackOfBookDto track = trackOfBook.get();
        if(track.getTakenStatus()){
            return ResponseEntity.badRequest();
        }
        track.setTakenStatus(true);
        track.setReaderName(username);
        track.setDateOfTake(LocalDateTime.now());
        libraryService.makeTrack(track);
        return ResponseEntity.ok();
    }
    @PutMapping("/admin/deleteTrack/{username}/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Вычеркнуть запись")
    public ResponseEntity.BodyBuilder deleteTrack(@PathVariable String username, @PathVariable String id){
        if(!userService.existsUser(username)){
            return ResponseEntity.badRequest();
        }
        Optional<TrackOfBookDto> trackOfBook = libraryService.getTrackByBookIsbn(id);
        if(trackOfBook.isEmpty()){
            return ResponseEntity.badRequest();
        }
        TrackOfBookDto track = trackOfBook.get();
        if(!track.getReaderName().equals(username)){
            return ResponseEntity.badRequest();
        }
        track.setTakenStatus(false);
        track.setReaderName(null);
        track.setDateOfTake(null);
        libraryService.deleteTrack(track);
        return ResponseEntity.ok();
    }
}