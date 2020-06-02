package com.webfontaine.ghclient.service.impl;

import com.webfontaine.ghclient.domain.BookMarkDocument;
import com.webfontaine.ghclient.dto.api.BookMarkDto;
import com.webfontaine.ghclient.dto.api.StatisticDto;
import com.webfontaine.ghclient.exception.DocumentNotFoundException;
import com.webfontaine.ghclient.exception.DocumentAlreadyExistException;
import com.webfontaine.ghclient.exception.WrongParameterException;
import com.webfontaine.ghclient.helper.CacheHelper;
import com.webfontaine.ghclient.helper.JsonHelper;
import com.webfontaine.ghclient.mapper.CommonMapper;
import com.webfontaine.ghclient.repositiry.BookMarkDocumentRepository;
import com.webfontaine.ghclient.service.BookMarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

/**
 * Service class implementation for bookmarks
 */

@Service
@Slf4j
public class BookMarkServiceImpl implements BookMarkService {

    private final CacheHelper cacheHelper;

    private final BookMarkDocumentRepository repository;

    private final CommonMapper mapper;

    @Autowired
    public BookMarkServiceImpl(CacheHelper cacheHelper, BookMarkDocumentRepository repository, CommonMapper mapper) {
        this.cacheHelper = cacheHelper;
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Add bookmark to db
     *
     * @param cacheId    cache id for getting object from cache
     * @param bookMarkId unique id
     */
    @Transactional
    @Override
    public void add(String cacheId, String bookMarkId) {
        if (isBlank(bookMarkId) || isBlank(cacheId)) {
            log.error("The provided param is incorrect.");
            throw new WrongParameterException("bookMarkId or cacheId is incorrect");
        }

        StatisticDto statisticDto = cacheHelper.getById(cacheId);
        log.debug("Adding new bookmark with  id {}", bookMarkId);

        if (isNull(statisticDto)) {
            /** Note: depends on requirements it is possible to warn user, load from github and save */
            return;
        }
        if (repository.findById(bookMarkId).isPresent()) {
            throw new DocumentAlreadyExistException(String.format("The bookmark %s already used", bookMarkId));
        }
        BookMarkDocument bookMarkDocument = new BookMarkDocument(cacheId, bookMarkId,
                JsonHelper.parseToJson(statisticDto), LocalDateTime.now());
        repository.save(bookMarkDocument);
        log.debug("Saved new bookmark with id {} and with value {}", bookMarkDocument.getBookMarkId(), bookMarkDocument.toString());
    }

    /**
     * List all bookmarks
     *
     * @return list of bookmarks
     */
    @Override
    public List<BookMarkDto> findAll() {
        List<BookMarkDocument> documents = repository.findAll();
        List<BookMarkDto> bookMarkDtos = new ArrayList<>();
        documents.forEach(document -> bookMarkDtos.add(mapper.bokMarkDocumentToBookMarkDto(document)));
        return bookMarkDtos;
    }

    /**
     * Fine one bookmark by id.
     *
     * @param bookMarkId bookmark id
     * @return bookmark
     */
    @Override
    public BookMarkDto findById(String bookMarkId) {
        log.debug("Find bookmark by id {}", bookMarkId);
        return mapper.bokMarkDocumentToBookMarkDto(repository.findById(bookMarkId).orElseThrow(DocumentNotFoundException::new));
    }
}
