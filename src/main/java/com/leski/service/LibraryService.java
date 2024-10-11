package com.leski.service;

import com.leski.dto.BookDto;
import com.leski.dto.TrackOfBookDto;
import com.leski.model.Book;
import com.leski.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.description.annotation.AnnotationValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public LibraryService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    public void deleteTrackByBookIsbn(String bookIsbn){
        Book book = bookRepository.findById(bookIsbn).orElse(null);
        bookRepository.save(book);
    }
    public TrackOfBookDto getTrackByBookIsbn(String bookIsbn){
        Book book = bookRepository.findById(bookIsbn).filter(Book::getTakenStatus).orElse(null);
        TrackOfBookDto trackOfBookDto;
        if(book != null)
            trackOfBookDto = modelMapper.map(book, TrackOfBookDto.class);
        else
            return null;
        return trackOfBookDto;
    }
    public List<TrackOfBookDto> getAllTrackOfBooks(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);

        Page<Book> pageResult = bookRepository.findAllByTakenStatus(true, pageable);

        List<Book> tracks;

        if(pageResult.hasContent())
            tracks = pageResult.getContent();
        else
            return new ArrayList<>();

        return tracks.stream()
                .map(book -> modelMapper.map(book, TrackOfBookDto.class))
                .toList();
    }

    public List<BookDto> getAllFreeBooks(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);

        Page<Book> pagedResult = bookRepository.findAllByTakenStatus(false,pageable);
        List<Book> books;

        if(pagedResult.hasContent())
            books = pagedResult.getContent();
        else
            return new ArrayList<>();

        return books.stream()
                .map(freeBook -> modelMapper.map(freeBook, BookDto.class))
                .toList();
    }

    public TrackOfBookDto makeTrack(TrackOfBookDto trackOfBook) {
        Book book = bookRepository.findById(trackOfBook.getIsbn()).orElse(null);
        if(book == null)
            return null;
        book.setTakenStatus(trackOfBook.getTakenStatus());
        book.setDateOfTake(trackOfBook.getDateOfTake());
        book.setReaderName(trackOfBook.getReaderName());
        bookRepository.save(book);
        return modelMapper.map(book, TrackOfBookDto.class);
    }
    public boolean deleteTrack(TrackOfBookDto trackOfBook) {
        Book book = bookRepository.findById(trackOfBook.getIsbn()).orElse(null);
        if(book == null)
            return false;
        book.setReaderName(null);
        book.setDateOfTake(null);
        book.setTakenStatus(false);
        bookRepository.save(book);
        return true;
    }
}