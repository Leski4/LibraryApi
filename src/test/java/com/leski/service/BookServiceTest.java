package com.leski.service;

import com.leski.dto.BookDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService bookService;

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
    void getAllBooksTest() {
        var books = bookService.getAllBooks(0,10);
        assertNotNull(books);
    }

    @Test
    void getBookByIDTest(){
        var book = bookService.getBookById("978-0-452-28423-4");
        assertNotNull(book);
    }

    @Test
    void addBookTest(){
        bookService.addBook(new BookDto("978-0-14-143951-8","Pride and Prejudice",
                "Romance","Jane Austen",
                "A story of love and misunderstandings in early 19th century England."));
        assertTrue(bookService.existsBookById("978-0-14-143951-8"));
    }

    @Test
    void existsBookByIDTest(){
        assertTrue(bookService.existsBookById("978-0-452-28423-4"));
    }

    @Test
    void deleteByIDTest(){
        assertTrue(bookService.deleteById("978-3-16-148410-0"));
    }
}
