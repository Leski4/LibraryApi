package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.dto.BookUpdateDto;
import com.leski.model.Book;
import com.leski.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
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
        if(books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }
    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение книги по её id")
    public ResponseEntity<BookDto> getBookById(@PathVariable String id){
        BookDto book = bookService.getBookById(id);
        if(book == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
    @PostMapping("")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление книги")
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto book){
        bookService.addOrUpdateBook(book);
        if(!bookService.existsBookById(book.getIsbn()))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(book);
    }
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление книги")
    public ResponseEntity<Void> deleteById(@PathVariable String id){
        if(!bookService.existsBookById(id))
            return ResponseEntity.notFound().build();
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Редактирование книги по id")
    public ResponseEntity<BookDto> updateBook(@PathVariable String id, @RequestBody BookUpdateDto bookUpdateDto){
        BookDto book = bookService.getBookById(id);
        if(book == null)
            return ResponseEntity.notFound().build();
        book.setAuthor(bookUpdateDto.getAuthor());
        book.setName(bookUpdateDto.getName());
        book.setGenre(bookUpdateDto.getGenre());
        book.setDescription(bookUpdateDto.getDescription());
        if(book.getName() == null || book.getGenre() == null || book.getAuthor() == null)
            return ResponseEntity.badRequest().build();
        bookService.addOrUpdateBook(book);
        return ResponseEntity.ok(book);
    }
}