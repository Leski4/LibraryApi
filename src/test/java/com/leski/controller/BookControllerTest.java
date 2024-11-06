package com.leski.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leski.dto.BookDto;
import com.leski.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
class BookControllerTest{
    @Autowired
    private BookService bookService;
    @Autowired
    private BookController bookController;
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final String ID = "978-0-14-143951-8";

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void allBooksTest() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    void addBookTest() throws Exception{
        BookDto book = new BookDto(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)).andExpect(status().isOk());

        bookController.deleteById(ID);
    }

    @Test
    void addBookFailTest() throws Exception{
        BookDto book = new BookDto(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");

        bookController.addBook(book);

        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)).andExpect(status().isBadRequest());

        bookController.deleteById(ID);
    }

    @Test
    void updateBookTest() throws Exception{
        BookDto book = new BookDto(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        BookDto bookUpdate = new BookDto(ID,"Pride",
                "Romance", "Austen", "A story of love.");
        String bookUpdateJson = objectMapper.writeValueAsString(bookUpdate);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookUpdateJson))
                .andExpect(status().isOk());

        bookController.deleteById(ID);
    }

    @Test
    void updateBookFailTest() throws Exception{
        BookDto bookUpdate = new BookDto(ID,"Pride",
                "Romance", "Austen", "A story of love.");
        String bookUpdateJson = objectMapper.writeValueAsString(bookUpdate);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookUpdateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBookTest() throws Exception{
        BookDto book = new BookDto(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}",ID))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteBookFailTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}",ID))
                .andExpect(status().isNotFound());
    }
}
