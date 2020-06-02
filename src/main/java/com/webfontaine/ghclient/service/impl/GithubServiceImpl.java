package com.webfontaine.ghclient.service.impl;

import com.webfontaine.ghclient.client.GithubClient;
import com.webfontaine.ghclient.dto.api.*;
import com.webfontaine.ghclient.dto.client.GithubCommitResponseDto;
import com.webfontaine.ghclient.dto.client.GithubSearchResponsesDto;
import com.webfontaine.ghclient.exception.WrongParameterException;
import com.webfontaine.ghclient.helper.CacheHelper;
import com.webfontaine.ghclient.helper.Helper;
import com.webfontaine.ghclient.mapper.CommonMapper;
import com.webfontaine.ghclient.service.GithubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

/**
 * The implementation of  GithubService
 * <p>
 * Service class for working with github data
 */
@Service
@Slf4j
public class GithubServiceImpl implements GithubService {

    private final GithubClient githubClient;

    private final CommonMapper commonMapper;

    private final CacheHelper cacheHelper;

    private static final int COMMITS_COUNT = 100;

    @Autowired
    public GithubServiceImpl(GithubClient githubClient, CommonMapper commonMapper, CacheHelper cacheHelper) {
        this.githubClient = githubClient;
        this.commonMapper = commonMapper;
        this.cacheHelper = cacheHelper;
    }

    @Override
    public SearchResponsesDto searchRepositories(String query, int start, int count) {
        if (isBlank(query) || start == 0) {
            throw new WrongParameterException("The search query should'not be null or empty");
        }
        GithubSearchResponsesDto searchResponsesDto = githubClient.searchRepos(query, start, count);
        if (isNull(searchResponsesDto)) {
            return new SearchResponsesDto(0, new ArrayList<>());
        }
        return new SearchResponsesDto(searchResponsesDto.getTotalCount(),
                commonMapper.githubSearchResponseToItemsDto(searchResponsesDto.getItems()));
    }

    /**
     * Retrieving committers list , impact user on project , and commit timeline
     *
     * @param repoName repository name
     * @return StatisticDto
     */
    @Override
    public StatisticDto generateStatisticForRepo(String repoName) {

        log.debug("Building statistic info for repository {}", repoName);

        if (isBlank(repoName)) {
            throw new WrongParameterException("The repository name should'not be null or empty");
        }

        List<GithubCommitResponseDto> commits = githubClient.getCommits(repoName);
        if (isNull(commits)) {
            return new StatisticDto();
        }

        Set<UserDto> committersDtos = getCommittersFromCommits(commits);

        StatisticDto statisticDto = createRepoStatistic(commits.stream().limit(COMMITS_COUNT).collect(Collectors.toList()));
        statisticDto.setCommitters(committersDtos);
        statisticDto.setRepoName(repoName);
        statisticDto.setCacheId(Helper.generateUUID());

        cacheHelper.addToCache(statisticDto);

        log.debug("Saved result in cache with id {} and with value {}", statisticDto.getCacheId(), statisticDto.toString());

        return statisticDto;
    }

    /**
     * The method for retrieving committer list
     *
     * @param commits commits on project
     * @return set of users
     */
    private Set<UserDto> getCommittersFromCommits(List<GithubCommitResponseDto> commits) {
        return commits.stream().map(githubCommitResponseDto ->
                commonMapper.committerDtoToUserDto(githubCommitResponseDto.getCommit().getCommitter())).
                collect(Collectors.toSet());
    }

    /**
     * Creating statistic about user impact on project and commits timeline
     *
     * @param commits commits on project
     * @return statistic
     */
    private StatisticDto createRepoStatistic(List<GithubCommitResponseDto> commits) {
        Map<String, UserCommitsActivityDto> impact = new HashMap<>();
        Map<String, List<CommitInfoDto>> commitTimeline = new HashMap<>();
        commits.stream().map(GithubCommitResponseDto::getCommit).forEach(commit -> {

            //Count commits in timeline
            String commitDate = commit.getCommitter().getDate().toLocalDate().toString();
            List<CommitInfoDto> infoDtos = commitTimeline.get(commitDate);

            CommitInfoDto commitInfoDto = new CommitInfoDto(commit.getCommitter().getName(), commit.getMessage(),
                    commit.getCommitter().getDate());

            if (isNull(infoDtos)) {
                List<CommitInfoDto> dtoList = new ArrayList<>();
                dtoList.add(commitInfoDto);
                commitTimeline.put(commitDate, dtoList);
            } else {
                infoDtos.add(commitInfoDto);
            }

            //Count impact user on project with percent
            String commiterName = commit.getCommitter().getName() + "/" + commit.getCommitter().getEmail();
            UserCommitsActivityDto count = impact.get(commiterName);
            if (isNull(count)) {
                impact.put(commiterName, new UserCommitsActivityDto(
                        1, countPercent(commits.size(), 1)));
            } else {
                count.setCount(count.getCount() + 1);
                count.setPercent(countPercent(commits.size(), count.getCount()));
                impact.put(commiterName, count);
            }
        });
        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setImpact(impact);
        statisticDto.setCommitsTimeline(commitTimeline);

        return statisticDto;
    }

    /**
     * Count percent
     *
     * @param length size of array
     * @param count  commits count
     * @return percent
     */
    private BigDecimal countPercent(int length, int count) {
        return new BigDecimal(count * 100 / (double) length).setScale(2, RoundingMode.HALF_UP);
    }

}
