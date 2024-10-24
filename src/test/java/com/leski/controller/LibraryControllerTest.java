package com.leski.controller;

import com.leski.dto.BookDto;
import com.leski.model.Book;
import com.leski.repository.BookRepository;
import com.leski.security.JwtAuthenticationFilter;
import com.leski.service.LibraryService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestHeader;

import static java.lang.String.format;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
public class LibraryControllerTest {
    @Autowired
    LibraryController libraryController;
    @Autowired
    private BookController bookController;
    @Autowired
    private BookRepository bookRepository;
    private MockMvc mockMvc;
    private final String ID = "978-0-14-143951-8";
    private final String USERNAME = "user";

    private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoiVVNFUiJ9.fCne0vHf9Cu4PWHwCZYAbdMtxXTL2A1yXXuSrn0NJyk";

    @Autowired
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() throws Exception {
        this.token = "Bearer " + token;
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController)
                .addFilter(filter)
                .build();
    }

    @Test
    void getFreeBooks() throws Exception{
        mockMvc.perform(get("/tracks"))
                .andExpect(status().isOk());
    }

    @Test
    void takeBookTestWithJWT() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        mockMvc.perform(post("/tracks/{id}",ID)
                        .header("Authorization", this.token))
                        .andExpect(status().isOk());

        bookRepository.deleteById(ID);
    }

    @Test
    void getTracksOfBooksTest() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        libraryController.makeTrack(USERNAME,ID);

        mockMvc.perform(get("/tracks/admin"))
                .andExpect(status().isOk());

        bookRepository.deleteById(ID);
    }

    @Test
    void returnBookTestWithJWT() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        libraryController.makeTrack(USERNAME,ID);

        mockMvc.perform(delete("/tracks/{id}", ID)
                        .header("Authorization", this.token))
                .andExpect(status().isNoContent());

        bookRepository.deleteById(ID);
    }

    @Test
    void makeTrackTest() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        mockMvc.perform(post("/tracks/admin/{username}/{id}",USERNAME, ID))
                .andExpect(status().isOk());

        bookRepository.deleteById(ID);
    }

    @Test
    void makeTrackFailTest() throws Exception{
        mockMvc.perform(post("/tracks/admin/{username}/{id}",USERNAME, ID))
                .andExpect(status().isNotFound());
    }
    @Test
    void deleteTrackTest() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        libraryController.makeTrack(USERNAME,ID);

        mockMvc.perform(delete("/tracks/admin/{username}/{id}", USERNAME, ID))
                .andExpect(status().isNoContent());

        bookRepository.deleteById(ID);
    }

    @Test
    void deleteTrackFailTest() throws Exception{
        Book book = new Book(ID,"Pride and Prejudice","Romance","Jane Austen"
                ,"A story of love and misunderstandings in early 19th century England.");
        bookController.addBook(book);

        mockMvc.perform(delete("/tracks/admin/{username}/{id}", USERNAME, ID))
                .andExpect(status().isNotFound());

        bookRepository.deleteById(ID);
    }
}