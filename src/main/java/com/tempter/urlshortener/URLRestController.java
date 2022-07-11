package com.tempter.urlshortener;

import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

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
            throw new RuntimeException("There is no shorter URL for : " + id);
        }
        return url;
    }

    @PostMapping("/create")
    public String create(@RequestBody String url) {

        if (!urlValidator.isValid(url)) {
            throw new RuntimeException("URL Invalid: " + url);
        }
        String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
        redisTemplate.opsForValue().set(id, url);
        return id;

    }

}
