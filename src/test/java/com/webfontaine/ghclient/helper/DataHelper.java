package com.webfontaine.ghclient.helper;

import com.webfontaine.ghclient.domain.BookMarkDocument;
import com.webfontaine.ghclient.dto.api.*;
import com.webfontaine.ghclient.dto.client.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Helper class for generating mock data
 */
public class DataHelper {

    public static final String STATISTIC_JSON_VALUE = "{\"cacheId\":\"cache-id\",\"repoName\":\"tuyn-repo\"," +
            "\"committers\":[{\"name\":\"Petros\",\"email\":\"petros@test.test\"},{\"name\":\"Poxos\"," +
            "\"email\":\"poxos@test.test\"}],\"impact\":{\"Petros/petros@test.test\":{\"count\":1,\"percent\":50}," +
            "\"Poxos/poxos@test.test\":{\"count\":1,\"percent\":50}},\"commitsTimeline\":{\"2020-05-30\":" +
            "[{\"commiterName\":\"Poxos\",\"message\":\"test Poxos commit message\",\"date\":[2020,5,30,16,13,31,421000000]}," +
            "{\"commiterName\":\"Petros\",\"message\":\"test Petros commit message\",\"date\":[2020,5,30,16,13,31,421000000]}]}}";


    public static BookMarkDocument buildBookMarkDocument() {
        BookMarkDocument document = new BookMarkDocument();
        document.setBookMarkId("bookmark-1");
        document.setCacheId("cache-id");
        document.setJsonValue(STATISTIC_JSON_VALUE);
        document.setCreatedDate(LocalDateTime.now().minusDays(1));
        return document;
    }

    public static BookMarkDocument buildBookMarkDocumentWithWrongJson() {
        BookMarkDocument document = new BookMarkDocument();
        document.setBookMarkId("bookmark-1");
        document.setCacheId("cache-id");
        document.setJsonValue("Poxos/poxos@test.test\":{\"count\":1,\"percent\":50}}");
        document.setCreatedDate(LocalDateTime.now().minusDays(1));
        return document;
    }

    public static GithubSearchResponsesDto buildGithubSearchResponsesDto() {
        GithubSearchResponsesDto gResponse = new GithubSearchResponsesDto();
        gResponse.setItems(buildGithubSearchResponseItemDtoList());
        gResponse.setTotalCount(5);
        return gResponse;
    }

    public static List<GithubSearchResponseItemDto> buildGithubSearchResponseItemDtoList() {
        GithubSearchResponseItemDto itemDto1 = new GithubSearchResponseItemDto(1, "Repo-1");
        GithubSearchResponseItemDto itemDto2 = new GithubSearchResponseItemDto(2, "Repo-2");
        GithubSearchResponseItemDto itemDto3 = new GithubSearchResponseItemDto(3, "Repo-3");
        GithubSearchResponseItemDto itemDto4 = new GithubSearchResponseItemDto(4, "Repo-4");
        GithubSearchResponseItemDto itemDto5 = new GithubSearchResponseItemDto(5, "Repo-5");
        return Arrays.asList(itemDto1, itemDto2, itemDto3, itemDto4, itemDto5);
    }

    public static GithubCommitResponseDto buildGithubCommitResponseDto(int index) {
        return new GithubCommitResponseDto("test-node-id-" + index, "sha-id-" + index,
                buildGithubCommitDto(index));
    }

    public static GithubCommitDto buildGithubCommitDto(int index) {
        return new GithubCommitDto("Test commit message " + index, buildGithubCommitterDto(index));
    }


    public static GithubCommitterDto buildGithubCommitterDto(int index) {
        return new GithubCommitterDto("Committer-" + index,
                "committer" + index + "@test.test", LocalDateTime.now().minusDays(10));
    }

    public static StatisticDto buildStatisticDto() {
        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setCacheId("cache-id");
        statisticDto.setRepoName("tuyn-repo");

        List<CommitInfoDto> infoDtos = Arrays.asList(
                new CommitInfoDto("Poxos", "test Poxos commit message", LocalDateTime.now().minusDays(2)),
                new CommitInfoDto("Petros", "test Petros commit message", LocalDateTime.now().minusDays(2)));
        Map<String, List<CommitInfoDto>> commitsTimeline = new HashMap<>();
        commitsTimeline.put(LocalDate.now().minusDays(2).toString(), infoDtos);
        statisticDto.setCommitsTimeline(commitsTimeline);

        Set<UserDto> userDtos = new HashSet<>();
        userDtos.add(new UserDto("Poxos", "poxos@test.test"));
        userDtos.add(new UserDto("Petros", "petros@test.test"));
        statisticDto.setCommitters(userDtos);

        Map<String, UserCommitsActivityDto> impact = new HashMap<>();
        impact.put("Poxos/poxos@test.test", new UserCommitsActivityDto(1, new BigDecimal(50)));
        impact.put("Petros/petros@test.test", new UserCommitsActivityDto(1, new BigDecimal(50)));
        statisticDto.setImpact(impact);

        return statisticDto;
    }
}
