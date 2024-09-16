package com.leski.service;

import com.leski.model.TrackOfBook;
import com.leski.repository.TrackOfBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibraryService {
    private final TrackOfBookRepository trackOfBookRepository;
    @Autowired
    public LibraryService(TrackOfBookRepository trackOfBookRepository){
        this.trackOfBookRepository = trackOfBookRepository;
    }
    public void addTrack(TrackOfBook trackOfBook){
        trackOfBookRepository.save(trackOfBook);
    }
    public void deleteTrackByBookId(int bookId){
        TrackOfBook trackOfBook = trackOfBookRepository.findByBookId(bookId);
        trackOfBookRepository.delete(trackOfBook);
    }
    public TrackOfBook getTrackByBookId(int bookId){
        return trackOfBookRepository.findByBookId(bookId);
    }
    public List<TrackOfBook> getAllTrackOfBooks(){
        return trackOfBookRepository.findAll();
    }
}