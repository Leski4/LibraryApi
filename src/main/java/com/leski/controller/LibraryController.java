package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.dto.TrackOfBookDto;
import com.leski.service.LibraryService;
import com.leski.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/tracks")
@Tag(name = "Учёт библиотечных книг")
@Slf4j
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
        if(freeBooks.isEmpty()){
            log.info("Page of free books by number N " + pageNo + " is empty.");
            return ResponseEntity.noContent().build();
        }
        log.info("Free books from page N " + pageNo + " were received.");
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
        {
            log.info("Page of tracks of books by number N " + pageNo + " is empty.");
            return ResponseEntity.noContent().build();
        }
        log.info("Tracks of books from page N " + pageNo + " were received.");
        return ResponseEntity.ok(trackOfBooks);
    }
    @PostMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Взять книгу")
    public ResponseEntity<TrackOfBookDto> takeBook(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        TrackOfBookDto trackOfBook = new TrackOfBookDto(id, true, LocalDateTime.now(), username);
        trackOfBook = libraryService.makeTrack(trackOfBook);
        if(trackOfBook == null)
        {
            log.warn("User " + username + " didn't take book by ID: " + id + ".");
            return ResponseEntity.notFound().build();
        }
        log.info("User " + username + " took book by ID: " + id + ".");
        return ResponseEntity.ok(trackOfBook);
    }
    @DeleteMapping("/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Вернуть книгу")
    public ResponseEntity<Void> returnBook(@PathVariable String id){
        TrackOfBookDto trackOfBook = libraryService.getTrackByBookIsbn(id);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(trackOfBook == null)
        {
            log.warn("User " + username + " does not has the book by ID: " + id + ".");
            return ResponseEntity.noContent().build();
        }
        if(!Objects.equals(trackOfBook.getReaderName(), username)) {
            log.warn("User " + username + " cannot return book by ID: " + id + ".");
            return ResponseEntity.noContent().build();
        }
        boolean result = libraryService.deleteTrack(trackOfBook);
        if(!result)
        {
            log.warn("Book by ID " + id + " didn't return.");
            return ResponseEntity.noContent().build();
        }
        log.info("User: " + username + " returned book by ID: " + id + ".");
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/admin/{username}/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Выдать книгу читателю")
    public ResponseEntity<TrackOfBookDto> makeTrack(@PathVariable String username, @PathVariable String id){
        if(!userService.existsUser(username)){
            log.warn("User: " + username + " does not exist.");
            return ResponseEntity.notFound().build();
        }
        TrackOfBookDto trackOfBook = new TrackOfBookDto(id, true, LocalDateTime.now(),username);
        trackOfBook = libraryService.makeTrack(trackOfBook);
        if(trackOfBook == null)
        {
            log.warn("Book by ID " + id + " is not free or even does not exist.");
            return ResponseEntity.notFound().build();
        }
        log.info("User " + username + " has been got book by ID: " + id + ".");
        return ResponseEntity.ok(trackOfBook);
    }
    @DeleteMapping("/admin/{username}/{id}")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "Вычеркнуть запись")
    public ResponseEntity<Void> deleteTrack(@PathVariable String username, @PathVariable String id){
        if(!userService.existsUser(username)){
            log.warn("User: " + username + " does not exist.");
            return ResponseEntity.noContent().build();
        }
        TrackOfBookDto trackOfBook = libraryService.getTrackByBookIsbn(id);
        if(trackOfBook == null){
            log.warn("User by name " + username + " does not has the book by ID: " + id + ".");
            return ResponseEntity.noContent().build();
        }
        boolean result = libraryService.deleteTrack(trackOfBook);
        if(!result){
            log.warn("Book by ID " + id + " didn't return.");
            return ResponseEntity.noContent().build();
        }
        log.info("Book by ID " + id + " was returned.");
        return ResponseEntity.noContent().build();
    }
}