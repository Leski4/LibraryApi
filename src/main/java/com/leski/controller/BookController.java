package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.dto.BookUpdateDto;
import com.leski.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/books")
@Tag(name = "Операции над книгами")
@Slf4j
public class BookController {
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }
    @GetMapping("")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение всех книг")
    public ResponseEntity<List<BookDto>> getBooks(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        List<BookDto> books = bookService.getAllBooks(pageNo, pageSize);
        if(books.isEmpty()){
            log.info("Page N " + pageNo + " is empty.");
            return ResponseEntity.noContent().build();
        }
        log.info("Books from page N " + pageNo + " were received.");
        return ResponseEntity.ok(books);
    }
    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение книги по её id")
    public ResponseEntity<BookDto> getBookById(@PathVariable String id){
        BookDto book = bookService.getBookById(id);
        if(book == null){
            log.warn("Book by ID " + id + " does not exist.");
            return ResponseEntity.notFound().build();
        }
        log.info("Book by ID " + id + " was received.");
        return ResponseEntity.ok(book);
    }
    @PostMapping("")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление книги")
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto book){
        if(bookService.existsBookById(book.getIsbn()))
        {
            log.warn("Book by ID " + book.getIsbn() + " is already exists.");
            return ResponseEntity.badRequest().build();
        }
        bookService.addBook(book);
        log.info("Book by name " + book.getName() + " was added.");
        return ResponseEntity.ok(book);
    }
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление книги")
    public ResponseEntity<Void> deleteById(@PathVariable String id){
        if(!bookService.existsBookById(id))
        {
            log.warn("Book by ID " + id + " does not exist.");
            return ResponseEntity.noContent().build();
        }
        bookService.deleteById(id);
        log.info("Book by ID " + id + " was deleted.");
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Редактирование книги по id")
    public ResponseEntity<BookDto> updateBook(@PathVariable String id, @RequestBody BookUpdateDto bookUpdateDto){
        BookDto book = bookService.getBookById(id);
        if(book == null)
        {
            log.warn("Book by ID " + id + " does not exist.");
            return ResponseEntity.notFound().build();
        }
        book.setAuthor(bookUpdateDto.getAuthor());
        book.setName(bookUpdateDto.getName());
        book.setGenre(bookUpdateDto.getGenre());
        book.setDescription(bookUpdateDto.getDescription());
        if(book.getName().isEmpty() || book.getGenre().isEmpty()  || book.getAuthor().isEmpty() ){
            log.warn("Fields should be not null.");
            return ResponseEntity.notFound().build();
        }
        bookService.updateBook(book);
        log.info("Book by ID " + id + " was updated.");
        return ResponseEntity.ok(book);
    }
}