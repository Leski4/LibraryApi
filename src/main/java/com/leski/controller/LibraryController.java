package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.dto.TrackOfBookDto;
import com.leski.model.Book;
import com.leski.service.LibraryService;
import com.leski.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aspectj.lang.annotation.Before;
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
@RequestMapping("/tracks")
@Tag(name = "Учёт библиотечных книг")
public class LibraryController {
    private final LibraryService libraryService;
    private final UserService userService;
    @Autowired
    public LibraryController(LibraryService libraryService, UserService userService){
        this.libraryService = libraryService;
        this.userService = userService;
    }
    @GetMapping("")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение списка свободных книг")
    public ResponseEntity<List<BookDto>> getAllFreeBooks(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        List<BookDto> freeBooks = libraryService.getAllFreeBooks(pageNo, pageSize);
        if(freeBooks.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(freeBooks);
    }
    @GetMapping("/admin")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Учёт книг")
    public ResponseEntity<List<TrackOfBookDto>> getAllTracks(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        List<TrackOfBookDto> trackOfBooks = libraryService.getAllTrackOfBooks(pageNo, pageSize);
        if(trackOfBooks.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(trackOfBooks);
    }
    @PostMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Взять книгу")
    public ResponseEntity<TrackOfBookDto> takeBook(@PathVariable String id) {
        TrackOfBookDto trackOfBook = libraryService.getTrackByBookIsbn(id);
        if(trackOfBook != null)
            return ResponseEntity.notFound().build();
        trackOfBook = new TrackOfBookDto(id, true, LocalDateTime.now(),
                SecurityContextHolder.getContext().getAuthentication().getName());
        libraryService.makeTrack(trackOfBook);
        return ResponseEntity.ok(trackOfBook);
    }
    @DeleteMapping("/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Вернуть книгу")
    public ResponseEntity<Void> returnBook(@PathVariable String id){
        TrackOfBookDto trackOfBook = libraryService.getTrackByBookIsbn(id);
        if(trackOfBook == null)
            return ResponseEntity.notFound().build();
        if(!Objects.equals(trackOfBook.getReaderName(),
                SecurityContextHolder.getContext().getAuthentication().getName())) {
            return ResponseEntity.notFound().build();
        }
        boolean result = libraryService.deleteTrack(trackOfBook);
        if(!result)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
    @PostMapping("/admin/{username}/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Выдать книгу читателю")
    public ResponseEntity<TrackOfBookDto> makeTrack(@PathVariable String username, @PathVariable String id){
        if(!userService.existsUser(username)){
            return ResponseEntity.notFound().build();
        }
        TrackOfBookDto trackOfBook = new TrackOfBookDto(id, true, LocalDateTime.now(),username);
        trackOfBook = libraryService.makeTrack(trackOfBook);
        if(trackOfBook == null)
            ResponseEntity.notFound().build();
        return ResponseEntity.ok(trackOfBook);
    }
    @DeleteMapping("/admin/{username}/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Вычеркнуть запись")
    public ResponseEntity<Void> deleteTrack(@PathVariable String username, @PathVariable String id){
        if(!userService.existsUser(username)){
            return ResponseEntity.notFound().build();
        }
        TrackOfBookDto trackOfBook = libraryService.getTrackByBookIsbn(id);
        if(trackOfBook == null){
            return ResponseEntity.notFound().build();
        }
        boolean result = libraryService.deleteTrack(trackOfBook);
        if(!result)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
}