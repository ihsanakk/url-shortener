package com.tempter.urlshortener;

import com.tempter.urlshortener.service.URLService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/url")
public class URLRestController {

    private final URLService urlService;

    @GetMapping("/{id}")
    public String getUrl(@PathVariable String id) {
        return urlService.getURL(id);
    }

    @PostMapping("/create")
    public String create(@RequestBody String url) {
        return urlService.create(url);
    }

}
