package com.leski.repository;

import com.leski.model.Status;
import com.leski.model.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {
    Page<Track> findAllByStatus(Status status, Pageable pageable);
}
