package com.webfontaine.ghclient.dto.api;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * The BookMarkDto
 */
@Data
public class BookMarkDto {

    private String bookMarkId;

    private StatisticDto statistic;

    private LocalDateTime createdDate;

}
