package com.webfontaine.ghclient.service;

import com.webfontaine.ghclient.dto.api.BookMarkDto;

import java.util.List;

/**
 * The BookMark service interface
 */
public interface BookMarkService {

    void add(String cacheId, String bookMarkId);

    List<BookMarkDto> findAll();

    BookMarkDto findById(String bookMarkId);
}
