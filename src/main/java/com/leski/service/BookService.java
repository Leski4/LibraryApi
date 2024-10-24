package com.leski.service;

import com.leski.dto.BookDto;
import com.leski.model.Book;
import com.leski.model.Status;
import com.leski.model.Track;
import com.leski.repository.BookRepository;
import com.leski.repository.TrackRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final TrackRepository trackRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public BookService(BookRepository bookRepository, TrackRepository trackRepository){
        this.bookRepository = bookRepository;
        this.trackRepository = trackRepository;
    }
    public List<Book> getAllBooks(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Book> pagedResult = bookRepository.findAll(pageable);
        List<Book> books;

        if(pagedResult.hasContent()){
            books = pagedResult.getContent();
        }else
            return null;

        return books;
    }
    public BookDto getBookById(String id){
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null)
            return null;
        return modelMapper.map(book, BookDto.class);
    }
    public boolean existsBookById(String id){
        return bookRepository.existsByIsbn(id);
    }
    public void addBook(Book book){
        bookRepository.save(book);
        Track track = new Track(book.getIsbn(), Status.IS_FREE,
                null,null, book);
        trackRepository.save(track);
    }
    public void updateBook(Book book){
        bookRepository.save(book);
    }
    public boolean deleteById(String id){
        Track track = trackRepository.findById(id).orElse(null);
        if(track == null){
            return false;
        }
        if(track.getStatus().name().equals("IS_FREE")){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}