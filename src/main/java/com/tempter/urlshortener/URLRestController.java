package com.tempter.urlshortener;

import com.google.common.hash.Hashing;
import com.tempter.urlshortener.exception.exceptions.URLShortenerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/url")
public class URLRestController {

    private final StringRedisTemplate redisTemplate;
    UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    @GetMapping("/{id}")
    public String getUrl(@PathVariable String id) {

        String url = redisTemplate.opsForValue().get(id);
        if (url == null) {
            throw new URLShortenerException("There is no shorter URL for : " + id);
        }
        log.info("URL retrieved: "+ url);
        return url;
    }

    @PostMapping("/create")
    public String create(@RequestBody String url) {

        if (!urlValidator.isValid(url)) {
            throw new URLShortenerException("URL Invalid: " + url);
        }
        String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
        redisTemplate.opsForValue().set(id, url);
        log.info("URL ID generated: "+ id);
        return id;

    }

}
