package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.model.Book;
import com.leski.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
public class LibraryControllerTest {
    @Autowired
    LibraryService libraryService;

    @Autowired
    LibraryController libraryController;
    @Autowired
    private BookController bookController;

    private MockMvc mockMvc;
    private final String ID = "978-0-14-143951-8";
    private final String USERNAME = "user";


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
    }

    @Test
    void getFreeBooks() throws Exception{
        mockMvc.perform(get("/tracks"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = { "USER" })
    void takeBook() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        mockMvc.perform(post("/tracks/{id}",ID))
                .andExpect(status().isOk());

        bookController.deleteById(ID);
    }

    @Test
    void getTracksOfBooks() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        libraryController.makeTrack(USERNAME,ID);

        mockMvc.perform(get("/tracks/admin"))
                .andExpect(status().isOk());

        bookController.deleteById(ID);
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = { "USER" })
    void returnBook() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        libraryController.takeBook(ID);

        mockMvc.perform(delete("/tracks/{id}", ID))
                .andExpect(status().isNoContent());

        bookController.deleteById(ID);
    }

    @Test
    void makeTrack() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        mockMvc.perform(post("/tracks/admin/{username}/{id}",USERNAME, ID))
                .andExpect(status().isOk());

        bookController.deleteById(ID);
    }

    @Test
    void deleteTrack() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        libraryController.makeTrack(USERNAME,ID);

        mockMvc.perform(delete("/tracks/admin/{username}/{id}", USERNAME, ID))
                .andExpect(status().isNoContent());

        bookController.deleteById(ID);
    }
}