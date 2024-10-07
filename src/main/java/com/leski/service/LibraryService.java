package com.leski.service;

import com.leski.dto.TrackOfBookDto;
import com.leski.model.Book;
import com.leski.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Optional<Book> book = bookRepository.findById(bookIsbn);
        book.ifPresent(bookRepository::save);
    }
    public Optional<TrackOfBookDto> getTrackByBookIsbn(String bookIsbn){
        Book book = bookRepository.findById(bookIsbn).orElse(null);
        TrackOfBookDto trackOfBookDto = new TrackOfBookDto();
        if(book != null){
            trackOfBookDto = modelMapper.map(book, TrackOfBookDto.class);
        }
        return Optional.of(trackOfBookDto);
    }
    public List<TrackOfBookDto> getAllTrackOfBooks(){
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, TrackOfBookDto.class))
                .toList();
    }

    public TrackOfBookDto makeTrack(TrackOfBookDto trackOfBook) {
        Optional<Book> optionalBook = bookRepository.findById(trackOfBook.getIsbn());
        if(optionalBook.isEmpty()){
            return null;
        }
        Book book = optionalBook.get();
        book.setReaderName(trackOfBook.getReaderName());
        book.setDateOfTake(LocalDateTime.now());
        book.setTakenStatus(true);
        bookRepository.save(book);
        trackOfBook = modelMapper.map(book, TrackOfBookDto.class);
        return trackOfBook;
    }
    public TrackOfBookDto deleteTrack(TrackOfBookDto trackOfBook) {
        Optional<Book> optionalBook = bookRepository.findById(trackOfBook.getIsbn());
        if(optionalBook.isEmpty()){
            return null;
        }
        Book book = optionalBook.get();
        book.setReaderName(null);
        book.setDateOfTake(null);
        book.setTakenStatus(false);
        bookRepository.save(book);
        trackOfBook = modelMapper.map(book, TrackOfBookDto.class);
        return trackOfBook;
    }
}