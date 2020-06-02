package com.webfontaine.ghclient.client;

import com.webfontaine.ghclient.dto.client.GithubCommitResponseDto;
import com.webfontaine.ghclient.dto.client.GithubSearchResponsesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * The class for requesting Github api
 */
@Component
@Slf4j
public class GithubClient {

    private final RestTemplate restTemplate;

    private static final String REPOS_SEARCH_PREFIX = "/search/repositories";

    private static final String REPOS_PREFIX = "/repos";

    private static final String REPOS_COMMITS_PREFIX = "/commits";

    @Value("${github.api.url}")
    private String host;

    @Autowired
    public GithubClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Search repository in github
     *
     * @param query keyword
     * @param start page start
     * @param count page count
     * @return GithubSearchResponsesDto search result
     */
    public GithubSearchResponsesDto searchRepos(String query, int start, int count) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + REPOS_SEARCH_PREFIX);
        builder.queryParam("q", query).queryParam("page", start).queryParam("per_page", count);
        return restTemplate.getForObject(builder.build().encode().toString(), GithubSearchResponsesDto.class);
    }

    /**
     * Method for getting all commits in repository
     *
     * @param repoName repository name
     * @return list of commits
     */
    public List<GithubCommitResponseDto> getCommits(String repoName) {
        log.debug("retrieving commits for repository {}", repoName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + REPOS_PREFIX + "/" + repoName + REPOS_COMMITS_PREFIX);
        ResponseEntity<List<GithubCommitResponseDto>> responseEntity =
                restTemplate.exchange(builder.build().encode().toString(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<GithubCommitResponseDto>>() {
                        });
        return responseEntity.getBody();
    }
}
