package com.webfontaine.ghclient.integration;

import com.webfontaine.ghclient.domain.BookMarkDocument;
import com.webfontaine.ghclient.dto.api.BookMarkDto;
import com.webfontaine.ghclient.dto.api.StatisticDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.webfontaine.ghclient.helper.DataHelper.buildBookMarkDocument;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The integration test for BookMarkController
 */
public class BookMarkControllerTest extends CommonIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testAddBookmark(@Autowired MongoTemplate mongoTemplate) {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/repositories/statistic"));
        builder.queryParam("name", "asdf-vm/asdf-plugins");
        ResponseEntity<StatisticDto> response = restTemplate.exchange(
                builder.build().encode().toString(), HttpMethod.GET, entity,
                new ParameterizedTypeReference<StatisticDto>() {
                });

        StatisticDto result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getRepoName()).isEqualTo("asdf-vm/asdf-plugins");
        assertThat(result.getCacheId()).isNotNull();

        UriComponentsBuilder addBookmarkBuilder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/bookmarks"));
        addBookmarkBuilder.queryParam("cacheId", result.getCacheId()).queryParam("bookMarkId", "saved-1");

        restTemplate.exchange(
                addBookmarkBuilder.build().toString(), HttpMethod.POST, entity, Void.class);

        BookMarkDocument saved = mongoTemplate.findById("saved-1", BookMarkDocument.class);

        assertThat(saved).isNotNull();
        assertThat(saved.getBookMarkId()).isEqualTo("saved-1");
    }

    @Test
    public void testFindById(@Autowired MongoTemplate mongoTemplate) {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        mongoTemplate.save(buildBookMarkDocument(), "bookmarks");

        ResponseEntity<BookMarkDto> response = restTemplate.exchange(
                createURLWithPort("/bookmarks/bookmark-1"), HttpMethod.GET, entity,
                new ParameterizedTypeReference<BookMarkDto>() {
                });

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBookMarkId()).isEqualTo("bookmark-1");
    }

    @Test
    public void testFindAll(@Autowired MongoTemplate mongoTemplate) {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        BookMarkDocument dto1 = buildBookMarkDocument();
        BookMarkDocument dto2 = buildBookMarkDocument();
        dto2.setBookMarkId("bookmark-2");
        mongoTemplate.save(dto1, "bookmarks");
        mongoTemplate.save(dto2, "bookmarks");

        ResponseEntity<List<BookMarkDto>> response = restTemplate.exchange(
                createURLWithPort("/bookmarks"), HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<BookMarkDto>>() {
                });

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(0).getBookMarkId()).isEqualTo("bookmark-1");
        assertThat(response.getBody().get(1).getBookMarkId()).isEqualTo("bookmark-2");
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
