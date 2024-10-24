package com.leski.service;

import com.leski.dto.TrackOfBookDto;
import com.leski.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
public class LibraryServiceTest {
    @Autowired
    private LibraryService libraryService;

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void getTrackByBookIsbnTest(){
        var track = libraryService.getTrackByBookIsbn("978-3-16-148410-0");
        assertNotNull(track);
    }

    @Test
    void getAllTracksOfBooksTest(){
        var tracks = libraryService.getAllTracksOfBooks(0,10);
        assertNotNull(tracks);
    }

    @Test
    void getAllFreeBooksTest(){
        var books = libraryService.getAllFreeBooks(0,10);
        assertNotNull(books);
    }

    @Test
    void makeTrackTest(){
        var track = libraryService.makeTrack(new TrackOfBookDto("978-3-16-148410-0",
                Status.IS_BUSY, LocalDateTime.now(), "user"));
        assertNotNull(track);
    }

    @Test
    void deleteTrackTest(){
        assertTrue(libraryService.deleteTrack(new TrackOfBookDto("978-0-452-28423-4",
                Status.IS_FREE, null, null)));
    }
}
