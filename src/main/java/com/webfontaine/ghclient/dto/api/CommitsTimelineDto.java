package com.webfontaine.ghclient.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * The CommitsTimelineDto
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommitsTimelineDto {
    private Map<String, List<CommitInfoDto>> commitsTimeline;
}
