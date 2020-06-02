package com.webfontaine.ghclient.service;

import com.webfontaine.ghclient.client.GithubClient;
import com.webfontaine.ghclient.dto.api.SearchResponsesDto;
import com.webfontaine.ghclient.dto.api.StatisticDto;
import com.webfontaine.ghclient.dto.client.GithubCommitResponseDto;
import com.webfontaine.ghclient.dto.client.GithubSearchResponsesDto;
import com.webfontaine.ghclient.exception.WrongParameterException;
import com.webfontaine.ghclient.helper.CacheHelper;
import com.webfontaine.ghclient.mapper.CommonMapper;
import com.webfontaine.ghclient.mapper.CommonMapperImpl;
import com.webfontaine.ghclient.service.impl.GithubServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.webfontaine.ghclient.helper.DataHelper.buildGithubCommitResponseDto;
import static com.webfontaine.ghclient.helper.DataHelper.buildGithubSearchResponsesDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for GithubServiceImpl
 */
public class GithubServiceImplTest {

    private GithubClient githubClient;

    private CommonMapper commonMapper;

    private CacheHelper cacheHelper;

    private GithubService githubService;

    @BeforeEach
    void setUp() {
        githubClient = mock(GithubClient.class);
        commonMapper = new CommonMapperImpl();
        cacheHelper = mock(CacheHelper.class);
        githubService = new GithubServiceImpl(githubClient, commonMapper, cacheHelper);
    }

    @Test
    public void searchRepositories_test_success() {
        GithubSearchResponsesDto gResponse = buildGithubSearchResponsesDto();
        when(githubClient.searchRepos("Repo", 1, 10)).thenReturn(gResponse);
        SearchResponsesDto actual = githubService.searchRepositories("Repo", 1, 10);

        assertThat(actual).isNotNull();
        assertThat(actual.getTotalCount()).isEqualTo(5);
        assertThat(actual.getItems()).isNotNull();
        assertThat(actual.getItems().size()).isEqualTo(5);
        assertThat(actual.getItems().size()).isEqualTo(5);
        assertThat(actual.getItems().get(0).getId()).isEqualTo(1);
        assertThat(actual.getItems().get(0).getFullName()).isEqualTo("Repo-1");
    }

    @Test
    public void searchRepositories_test_null_items() {
        when(githubClient.searchRepos("Repo", 1, 10)).thenReturn(new GithubSearchResponsesDto(0, null));
        SearchResponsesDto actual = githubService.searchRepositories("Repo", 1, 10);

        assertThat(actual).isNotNull();
        assertThat(actual.getTotalCount()).isEqualTo(0);
        assertThat(actual.getItems()).isNull();
    }

    @Test
    public void searchRepositories_test_null_response() {
        when(githubClient.searchRepos("Repo", 1, 10)).thenReturn(null);
        SearchResponsesDto actual = githubService.searchRepositories("Repo", 1, 10);

        assertThat(actual).isNotNull();
        assertThat(actual.getTotalCount()).isEqualTo(0);
        assertThat(actual.getItems()).isNotNull();
        assertThat(actual.getItems().isEmpty()).isTrue();
    }

    @Test
    public void generateStatisticForRepo_test_success() {
        List<GithubCommitResponseDto> responseDtos = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            responseDtos.add(buildGithubCommitResponseDto(i));
        }
        when(githubClient.getCommits("utyu-repo")).thenReturn(responseDtos);
        StatisticDto actual = githubService.generateStatisticForRepo("utyu-repo");

        assertThat(actual).isNotNull();
        assertThat(actual.getRepoName()).isEqualTo("utyu-repo");
        assertThat(actual.getCommitters()).isNotNull();
        assertThat(actual.getCommitters().size()).isEqualTo(5);
        assertThat(actual.getCommitsTimeline()).isNotNull();
        assertThat(actual.getCommitsTimeline().size()).isEqualTo(1);
        assertThat(actual.getCommitsTimeline().
                get(LocalDateTime.now().minusDays(10).toLocalDate().toString())).isNotNull();
        assertThat(actual.getCommitsTimeline().
                get(LocalDateTime.now().minusDays(10).toLocalDate().toString()).size()).isEqualTo(5);
        assertThat(actual.getCommitsTimeline().
                get(LocalDateTime.now().minusDays(10).toLocalDate().toString()).get(0).getCommiterName()).isEqualTo("Committer-1");
        assertThat(actual.getCommitsTimeline().
                get(LocalDateTime.now().minusDays(10).toLocalDate().toString()).get(0).getMessage()).isEqualTo("Test commit message 1");

        assertThat(actual.getImpact()).isNotNull();
        assertThat(actual.getImpact().size()).isEqualTo(5);
        assertThat(actual.getImpact().get("Committer-2/committer2@test.test")).isNotNull();
        assertThat(actual.getImpact().get("Committer-2/committer2@test.test").getCount()).isEqualTo(1);
        assertThat(actual.getImpact().get("Committer-2/committer2@test.test").getPercent()).
                isEqualTo(new BigDecimal(20).setScale(2, RoundingMode.HALF_UP));
    }


    @Test
    public void generateStatisticForRepo_test_empty_response() {
        List<GithubCommitResponseDto> responseDtos = new ArrayList<>();
        when(githubClient.getCommits("utyu-repo")).thenReturn(responseDtos);
        StatisticDto actual = githubService.generateStatisticForRepo("utyu-repo");

        assertThat(actual).isNotNull();
        assertThat(actual.getRepoName()).isEqualTo("utyu-repo");
        assertThat(actual.getCommitters()).isNotNull();
        assertThat(actual.getCommitters().size()).isEqualTo(0);
        assertThat(actual.getCommitsTimeline()).isNotNull();
        assertThat(actual.getCommitsTimeline().size()).isEqualTo(0);
        assertThat(actual.getImpact()).isNotNull();
        assertThat(actual.getImpact().size()).isEqualTo(0);
    }

    @Test
    public void generateStatisticForRepo_test_null_response() {
        when(githubClient.getCommits("utyu-repo")).thenReturn(null);
        StatisticDto actual = githubService.generateStatisticForRepo("utyu-repo");
        assertThat(actual).isNotNull();
    }

    @Test
    public void generateStatisticForRepo_test_empty_param() {
        Assertions.assertThrows(WrongParameterException.class, () -> {
            githubService.generateStatisticForRepo("");
        });
    }
}
