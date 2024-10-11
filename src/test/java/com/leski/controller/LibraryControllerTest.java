package com.leski.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leski.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

    private MockMvc mockMvc;
    private final String ID = "978-0-14-118280-3";
    private final String USERNAME = "govroshka";

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
        mockMvc.perform(post("/tracks/{id}",ID))
                .andExpect(status().isOk());

        libraryController.returnBook(ID);
    }

    @Test
    void getTracksOfBooks() throws Exception{
        libraryController.makeTrack(USERNAME,ID);

        mockMvc.perform(get("/tracks/admin"))
                .andExpect(status().isOk());

        libraryController.deleteTrack(USERNAME,ID);
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = { "USER" })
    void returnBook() throws Exception{
        libraryController.takeBook(ID);

        mockMvc.perform(delete("/tracks/{id}", ID))
                .andExpect(status().isOk());
    }

    @Test
    void makeTrack() throws Exception{
        mockMvc.perform(post("/tracks/admin/{username}/{id}",USERNAME, ID))
                .andExpect(status().isOk());

        libraryController.deleteTrack(USERNAME,ID);
    }

    @Test
    void deleteTrack() throws Exception{
        libraryController.makeTrack(USERNAME,ID);

        mockMvc.perform(delete("/tracks/admin/{username}/{id}", USERNAME, ID))
                .andExpect(status().isOk());
    }
}