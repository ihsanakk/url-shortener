package com.tempter.urlshortener.service.impl;

import com.google.common.hash.Hashing;
import com.tempter.urlshortener.exception.exceptions.URLShortenerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class URLServiceImplTest {

    @InjectMocks
    URLServiceImpl urlService;

    @Mock
    StringRedisTemplate redisTemplate;

    @Mock
    ValueOperations<String, String> valueOps;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
    }

    @Test
    void getURL() {
        // normal case
        String exp = "test-url";
        when(valueOps.get(ArgumentMatchers.anyString())).thenReturn(exp);
        String actual = urlService.getURL("test-id");
        assertEquals(exp,actual);

        // exception
        String invalidID = "zzz";
        when(valueOps.get(invalidID)).thenThrow(new URLShortenerException("There is no shorter URL for"));
        Exception exception = assertThrows(URLShortenerException.class, () -> urlService.getURL(invalidID));

        assertTrue(exception.getMessage().contains("There is no shorter URL for"));
    }

    @Test
    void create() {
        // normal case
        String validRawURL = "http://test-url.com";
        String id = Hashing.murmur3_32().hashString(validRawURL, StandardCharsets.UTF_8).toString();
        doNothing().when(valueOps).set(ArgumentMatchers.anyString(),ArgumentMatchers.anyString());

        assertEquals(urlService.create(validRawURL),id);

        // exception
        String invalidURL = "xxx";
        Exception exception = assertThrows(URLShortenerException.class, () -> urlService.create(invalidURL));

        assertTrue(exception.getMessage().contains("URL Invalid"));

    }
}