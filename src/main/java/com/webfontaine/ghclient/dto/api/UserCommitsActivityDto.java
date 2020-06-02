package com.webfontaine.ghclient.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommitsActivityDto {

    private int count;

    private BigDecimal percent;
}
