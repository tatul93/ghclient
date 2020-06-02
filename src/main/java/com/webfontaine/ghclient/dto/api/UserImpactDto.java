package com.webfontaine.ghclient.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserImpactDto {

    private Map<String, UserCommitsActivityDto> impact;

}
