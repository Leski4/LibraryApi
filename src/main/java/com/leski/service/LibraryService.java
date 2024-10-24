package com.leski.service;

import com.leski.dto.TrackOfBookDto;
import com.leski.model.Book;
import com.leski.model.Status;
import com.leski.model.Track;
import com.leski.repository.TrackRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {
    private final TrackRepository trackRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public LibraryService(TrackRepository trackRepository){
        this.trackRepository = trackRepository;
    }
    public TrackOfBookDto getTrackByBookIsbn(String bookIsbn){
        Track track = trackRepository.findById(bookIsbn).filter(track1 -> track1.getStatus().name().equals("IS_BUSY")).orElse(null);
        TrackOfBookDto trackOfBookDto;
        if(track != null)
            trackOfBookDto = modelMapper.map(track, TrackOfBookDto.class);
        else
            return null;
        return trackOfBookDto;
    }
    public List<TrackOfBookDto> getAllTracksOfBooks(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);

        Page<Track> pageResult = trackRepository.findAll(pageable);

        List<Track> tracks;

        if(pageResult.hasContent())
            tracks = pageResult.getContent();
        else
            return null;

        return tracks.stream()
                .map(track -> modelMapper.map(track, TrackOfBookDto.class))
                .toList();
    }

    public List<Book> getAllFreeBooks(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);

        Page<Track> pagedResult = trackRepository.findAllByStatus(Status.IS_FREE, pageable);
        List<Track> freeBooks;

        if(pagedResult.hasContent())
            freeBooks = pagedResult.getContent();
        else
            return null;

        return freeBooks.stream()
                .map(Track::getBook)
                .toList();
    }

    public TrackOfBookDto makeTrack(TrackOfBookDto trackOfBook) {
        Track track = trackRepository.findById(trackOfBook.getIsbn()).
                filter(track1 -> track1.getStatus().name().equals("IS_FREE")).orElse(null);
        if(track == null)
            return null;
        track.setStatus(trackOfBook.getStatus());
        track.setDateOfTake(trackOfBook.getDateOfTake());
        track.setReaderName(trackOfBook.getReaderName());
        trackRepository.save(track);
        return modelMapper.map(track, TrackOfBookDto.class);
    }
    public boolean deleteTrack(TrackOfBookDto trackOfBook) {
        Track track = trackRepository.findById(trackOfBook.getIsbn()).orElse(null);
        if(track == null)
            return false;
        track.setStatus(Status.IS_FREE);
        track.setReaderName(null);
        track.setDateOfTake(null);
        trackRepository.save(track);
        return true;
    }
}