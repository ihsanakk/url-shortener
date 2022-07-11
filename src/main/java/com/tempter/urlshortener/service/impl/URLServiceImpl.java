package com.tempter.urlshortener.service.impl;

import com.google.common.hash.Hashing;
import com.tempter.urlshortener.exception.exceptions.URLShortenerException;
import com.tempter.urlshortener.service.URLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class URLServiceImpl implements URLService {

    private final StringRedisTemplate redisTemplate;
    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    @Override
    public String getURL(String id) {

        String url = redisTemplate.opsForValue().get(id);
        if (url == null) {
            throw new URLShortenerException("There is no shorter URL for : " + id);
        }
        log.info("URL retrieved: "+ url);
        return url;
    }

    @Override
    public String create(String url) {

        if (!urlValidator.isValid(url)) {
            throw new URLShortenerException("URL Invalid: " + url);
        }
        String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
        redisTemplate.opsForValue().set(id, url);
        log.info("URL ID generated: "+ id);
        return id;
    }
}
