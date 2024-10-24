package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.model.Book;
import com.leski.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Validated
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
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        List<Book> books = bookService.getAllBooks(pageNo, pageSize);
        if(books == null){
            log.info("Page N " + pageNo + " is empty.");
            return ResponseEntity.noContent().build();
        }
        log.info("Books from page N " + pageNo + " were received.");
        return ResponseEntity.ok(books);
    }
    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение книги по её id")
    public ResponseEntity<BookDto> getBookById(@PathVariable @Size(min = 17, max = 17) String id){
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
    public ResponseEntity<Book> addBook(@RequestBody @Valid Book book){
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
    public ResponseEntity<String> deleteById(@PathVariable @Size(min = 17, max = 17) String id){
        if(!bookService.existsBookById(id))
        {
            log.warn("Book by ID " + id + " does not exist.");
            return ResponseEntity.notFound().build();
        }
        boolean isDeleted = bookService.deleteById(id);
        if(!isDeleted){
            log.warn("Book by ID " + id + " is busy.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("IS_BUSY");
        }
        log.info("Book by ID " + id + " was deleted.");
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Редактирование книги по id")
    public ResponseEntity<Book> updateBook(@PathVariable @Size(min = 17, max = 17) String id,
                                           @RequestBody @Valid BookDto bookUpdateDto){
        if(!bookService.existsBookById(id))
        {
            log.warn("Book by ID " + id + " does not exist.");
            return ResponseEntity.notFound().build();
        }
        Book book = new Book();
        book.setIsbn(id);
        book.setAuthor(bookUpdateDto.getAuthor());
        book.setName(bookUpdateDto.getName());
        book.setGenre(bookUpdateDto.getGenre());
        book.setDescription(bookUpdateDto.getDescription());
        bookService.updateBook(book);
        log.info("Book by ID " + id + " was updated.");
        return ResponseEntity.ok(book);
    }
}