package com.tempter.urlshortener.service;

public interface URLService {

    String getURL(String id);
    String create(String url);

}
