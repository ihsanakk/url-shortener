package com.tempter.urlshortener;

import com.tempter.urlshortener.service.URLService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class URLRestControllerTest {

    @Autowired
    private URLService urlService;

    @Autowired
    private MockMvc mockMvc;

    private final List<String> ids = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // dummy records
        ids.add(urlService.create("https://test1.com"));
        ids.add(urlService.create("https://test2.com"));
        ids.add(urlService.create("https://test3.com"));
        ids.add(urlService.create("https://test4.com"));
        ids.add(urlService.create("https://test5.com"));
    }

    @Test
    void getUrl() throws Exception {
        // normal
        mockMvc.perform(get("/api/url/"+ ids.get(0)))
                .andExpect(status().isOk())
                        .andExpect(result -> assertEquals("https://test1.com", result.getResponse().getContentAsString()));

        // exception
        mockMvc.perform(get("/api/url/"+ "invalidID"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("There is no shorter URL for")));
    }

    @Test
    void create() throws Exception {
        // normal
        mockMvc.perform(post("/api/url/create").content("https://test1.com"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(8, result.getResponse().getContentAsString().length()));

        // exception
        mockMvc.perform(post("/api/url/create").content("xxx"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("URL Invalid")));
    }
}