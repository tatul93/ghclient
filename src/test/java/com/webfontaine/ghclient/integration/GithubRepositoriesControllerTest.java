package com.webfontaine.ghclient.integration;

import com.webfontaine.ghclient.dto.api.SearchResponsesDto;
import com.webfontaine.ghclient.dto.api.StatisticDto;
import org.junit.jupiter.api.Test;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * The integration test for GithubRepositoriesController
 */
public class GithubRepositoriesControllerTest extends CommonIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testSearchRepo() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/repositories/search"));
        builder.queryParam("query", "asdf").queryParam("start", 1).
                queryParam("count", 10);
        ResponseEntity<SearchResponsesDto> response = restTemplate.exchange(
                builder.build().encode().toString(), HttpMethod.GET, entity,
                new ParameterizedTypeReference<SearchResponsesDto>() {});

        SearchResponsesDto result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getItems()).isNotNull();
        assertThat(result.getTotalCount()).isNotNull();
        assertThat(result.getItems().size()).isEqualTo(10);
    }

    @Test
    public void testGetStatistic() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/repositories/statistic"));
        builder.queryParam("name", "asdf-vm/asdf-plugins");
        ResponseEntity<StatisticDto> response = restTemplate.exchange(
                builder.build().encode().toString(), HttpMethod.GET, entity,
                new ParameterizedTypeReference<StatisticDto>() {});

        StatisticDto result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getRepoName()).isEqualTo("asdf-vm/asdf-plugins");
        assertThat(result.getImpact()).isNotNull();
        assertThat(result.getCommitters()).isNotNull();
        assertThat(result.getCommitsTimeline()).isNotNull();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
