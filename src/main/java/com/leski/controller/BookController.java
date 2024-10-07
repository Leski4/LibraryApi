package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.model.Book;
import com.leski.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@Tag(name = "Операции над книгами")
@Slf4j
public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }
    @GetMapping("/books")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение всех книг")
    public ResponseEntity<List<Book>> getBooks(){
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    @GetMapping("/book/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение книги по её id")
    public ResponseEntity<Book> getBookById(@PathVariable String id){
        Book book = bookService.getBookById(id);
        /*BookDto bookDto = modelMapper.map(book,BookDto.class);*/
        return ResponseEntity.ok(book);
    }
    @PostMapping("/addBook")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление книги")
    public ResponseEntity<BookDto> addBook(@RequestBody Book book){
        BookDto bookDto = modelMapper.map(book,BookDto.class);
        bookService.addBook(book);

        /*TrackOfBook trackOfBook = typeMap.map(book);
        libraryService.addTrack(trackOfBook);*/
        return ResponseEntity.ok(bookDto);
    }
    @DeleteMapping("/book/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление книги")
    public void deleteById(@PathVariable String id){
        bookService.deleteById(id);
    }
    @PutMapping("/book/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Редактирование книги по id")
    public ResponseEntity<BookDto> updateBook(@PathVariable String id, @RequestBody BookDto bookDto){
        Book newVersionOfBook = modelMapper.map(bookDto,Book.class);
        Book laterVersionOfBook = bookService.getBookById(id);
        laterVersionOfBook.setAuthor(newVersionOfBook.getAuthor());
        laterVersionOfBook.setName(newVersionOfBook.getName());
        laterVersionOfBook.setGenre(newVersionOfBook.getGenre());
        laterVersionOfBook.setDescription(newVersionOfBook.getDescription());
        bookService.addBook(laterVersionOfBook);
        return ResponseEntity.ok(bookDto);
    }
}