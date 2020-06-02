package com.webfontaine.ghclient.service;

import com.webfontaine.ghclient.dto.api.SearchResponsesDto;
import com.webfontaine.ghclient.dto.api.StatisticDto;

/**
 * The Github service interface
 */
public interface GithubService {

    SearchResponsesDto searchRepositories(String query, int start, int count);

    StatisticDto generateStatisticForRepo(String repoName);
}
