package com.leski.repository;

import com.leski.model.TrackOfBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TrackOfBookRepository extends JpaRepository<TrackOfBook,String> {
   // TrackOfBook findByBookIsbn(String bookIsbn);
}
