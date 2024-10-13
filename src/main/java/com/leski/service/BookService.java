package com.leski.service;

import com.leski.dto.BookDto;
import com.leski.model.Book;
import com.leski.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    public List<BookDto> getAllBooks(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Book> pagedResult = bookRepository.findAll(pageable);
        List<Book> books;

        if(pagedResult.hasContent()){
            books = pagedResult.getContent();
        }else
            return new ArrayList<BookDto>();

        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }
    public BookDto getBookById(String id){
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null)
            return null;
        return modelMapper.map(book,BookDto.class);
    }
    public boolean existsBookById(String id){
        return bookRepository.existsByIsbn(id);
    }
    public void addBook(BookDto book){
        Book bookForAdd = modelMapper.map(book, Book.class);
        bookForAdd.setTakenStatus(false);
        bookRepository.save(bookForAdd);
    }
    public void updateBook(BookDto book){
        Optional<Book> optionalBook = bookRepository.findById(book.getIsbn());
        if(optionalBook.isEmpty())
            return;
        Book primaryBook = optionalBook.get();
        Book bookForUpdate = modelMapper.map(book, Book.class);
        bookForUpdate.setTakenStatus(primaryBook.getTakenStatus());
        bookForUpdate.setDateOfTake(primaryBook.getDateOfTake());
        bookForUpdate.setReaderName(primaryBook.getReaderName());
        bookRepository.save(bookForUpdate);
    }
    public void deleteById(String id){
        bookRepository.deleteById(id);
    }
}