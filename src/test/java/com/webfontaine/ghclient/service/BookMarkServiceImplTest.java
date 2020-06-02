package com.webfontaine.ghclient.service;

import com.webfontaine.ghclient.domain.BookMarkDocument;
import com.webfontaine.ghclient.dto.api.BookMarkDto;
import com.webfontaine.ghclient.dto.api.StatisticDto;
import com.webfontaine.ghclient.exception.DocumentAlreadyExistException;
import com.webfontaine.ghclient.exception.DocumentNotFoundException;
import com.webfontaine.ghclient.exception.WrongParameterException;
import com.webfontaine.ghclient.helper.CacheHelper;
import com.webfontaine.ghclient.mapper.CommonMapper;
import com.webfontaine.ghclient.mapper.CommonMapperImpl;
import com.webfontaine.ghclient.repositiry.BookMarkDocumentRepository;
import com.webfontaine.ghclient.service.impl.BookMarkServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.webfontaine.ghclient.helper.DataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * The test class for BookMarkServiceImpl
 */
public class BookMarkServiceImplTest {

    private CacheHelper cacheHelper;

    private BookMarkDocumentRepository repository;

    private CommonMapper mapper;

    private BookMarkService bookMarkService;

    @BeforeEach
    void setUp() {
        repository = mock(BookMarkDocumentRepository.class);
        mapper = new CommonMapperImpl();
        cacheHelper = mock(CacheHelper.class);
        bookMarkService = new BookMarkServiceImpl(cacheHelper, repository, mapper);
    }

    @Test
    public void add_test_success() {
        StatisticDto statisticDto = buildStatisticDto();
        when(cacheHelper.getById("cache-id")).thenReturn(statisticDto);
        when(repository.findById("bookmark-1")).thenReturn(Optional.empty());

        bookMarkService.add("cache-id", "bookmark-1");
        ArgumentCaptor<BookMarkDocument> argument = ArgumentCaptor.forClass(BookMarkDocument.class);
        verify(repository, times(1)).save(argument.capture());
        assertThat(argument.getValue().getBookMarkId()).isEqualTo("bookmark-1");
        assertThat(argument.getValue().getJsonValue()).isNotNull();
    }

    @Test
    public void add_test_invalid_id() {
        Assertions.assertThrows(WrongParameterException.class, () -> {
            bookMarkService.add("", "");
        });
    }

    @Test
    public void add_test_existing_bookmark() {
        StatisticDto statisticDto = buildStatisticDto();
        when(cacheHelper.getById("cache-id")).thenReturn(statisticDto);
        when(repository.findById("bookmark-1")).thenReturn(Optional.of(new BookMarkDocument()));
        Assertions.assertThrows(DocumentAlreadyExistException.class, () -> {
            bookMarkService.add("cache-id", "bookmark-1");
        });
    }

    @Test
    public void findAll_success() {
        when(repository.findAll()).thenReturn(Arrays.asList(buildBookMarkDocument()));
        List<BookMarkDto> result = bookMarkService.findAll();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getBookMarkId()).isEqualTo("bookmark-1");
        assertThat(result.get(0).getStatistic().getRepoName()).isEqualTo("tuyn-repo");
    }

    @Test
    public void findAll_wrongJson() {
        when(repository.findAll()).thenReturn(Arrays.asList(buildBookMarkDocumentWithWrongJson()));
        Assertions.assertThrows(RuntimeException.class, () -> {
            bookMarkService.findAll();
        });
    }

    @Test
    public void findById_success() {
        when(repository.findById("bookmark-1")).thenReturn(Optional.of(buildBookMarkDocument()));
        BookMarkDto result = bookMarkService.findById("bookmark-1");
        assertThat(result).isNotNull();
        assertThat(result.getBookMarkId()).isEqualTo("bookmark-1");
        assertThat(result.getStatistic().getRepoName()).isEqualTo("tuyn-repo");
    }

    @Test
    public void findById_not_found() {
        when(repository.findById("bookmark-1")).thenReturn(Optional.empty());
        Assertions.assertThrows(DocumentNotFoundException.class, () -> {
            bookMarkService.findById("bookmark-1");
        });
    }

    @Test
    public void findById_wrong_json() {
        when(repository.findById("bookmark-1")).thenReturn(Optional.of(buildBookMarkDocumentWithWrongJson()));
        Assertions.assertThrows(RuntimeException.class, () -> {
            bookMarkService.findById("bookmark-1");
        });
    }
}
