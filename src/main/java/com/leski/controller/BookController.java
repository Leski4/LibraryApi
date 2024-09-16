package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.model.Book;
import com.leski.model.TrackOfBook;
import com.leski.service.BookService;
import com.leski.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@Tag(name = "Операции над книгами")
public class BookController {
    private final BookService bookService;
    private final LibraryService libraryService;
    private final ModelMapper modelMapper = new ModelMapper();

    private final TypeMap<Book,TrackOfBook> typeMap = modelMapper.createTypeMap(Book.class,TrackOfBook.class);
    {
        typeMap.addMappings(mapper -> mapper.map(Book::getId,TrackOfBook::setBookId));
    }
    @Autowired
    public BookController(BookService bookService, LibraryService libraryService){
        this.bookService = bookService;
        this.libraryService = libraryService;
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
    public ResponseEntity<Book> getBookById(@PathVariable int id){
        Book book = bookService.getBookById(id);
        /*BookDto bookDto = modelMapper.map(book,BookDto.class);*/
        return ResponseEntity.ok(book);
    }
    @GetMapping("/book/byISBN/{isbn}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение книги по её isbn")
    public ResponseEntity<Book> getBooksByISBN(@PathVariable String isbn){
        Book book = bookService.getBookByISBN(isbn);
/*        BookDto bookDto = modelMapper.map(book,BookDto.class);*/
        return ResponseEntity.ok(book);
    }
    @PostMapping("/book")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление книги")
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto){
        Book book = modelMapper.map(bookDto,Book.class);
        bookService.addBook(book);

        TrackOfBook trackOfBook = typeMap.map(book);
        libraryService.addTrack(trackOfBook);
        return ResponseEntity.ok(bookDto);
    }
    @DeleteMapping("/book/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление книги")
    public void deleteById(@PathVariable int id){
        bookService.deleteById(id);
        libraryService.deleteTrackByBookId(id);
    }
    @PutMapping("/book/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Редактирование книги по id")
    public ResponseEntity<BookDto> updateBook(@PathVariable int id, @RequestBody BookDto bookDto){
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