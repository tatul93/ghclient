package com.webfontaine.ghclient.dto.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString
public class StatisticDto {

    private String cacheId;

    private String repoName;

    private Set<UserDto> committers;

    private Map<String, UserCommitsActivityDto> impact;

    private Map<String, List<CommitInfoDto>> commitsTimeline;

}
