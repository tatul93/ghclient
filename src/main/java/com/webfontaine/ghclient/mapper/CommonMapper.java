package com.webfontaine.ghclient.mapper;

import com.webfontaine.ghclient.domain.BookMarkDocument;
import com.webfontaine.ghclient.dto.api.*;
import com.webfontaine.ghclient.dto.client.GithubCommitterDto;
import com.webfontaine.ghclient.dto.client.GithubSearchResponseItemDto;
import com.webfontaine.ghclient.helper.JsonHelper;
import org.mapstruct.Mapper;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * The mapper class for dto mappings
 */
@Mapper(componentModel = "spring",  imports = JsonHelper.class)
public interface CommonMapper {

    UserDto committerDtoToUserDto(GithubCommitterDto githubCommitterDto);

    SearchResponseItemDto githubSearchResponseToItemDto(GithubSearchResponseItemDto githubSearchResponseItemDto);

    List<SearchResponseItemDto> githubSearchResponseToItemsDto(List<GithubSearchResponseItemDto> githubSearchResponseItemDto);

    default BookMarkDto bokMarkDocumentToBookMarkDto(BookMarkDocument document) {
        if (isNull(document)) {
            return null;
        }
        BookMarkDto bookMarkDto = new BookMarkDto();
        bookMarkDto.setBookMarkId(document.getBookMarkId());
        bookMarkDto.setCreatedDate(document.getCreatedDate());
        bookMarkDto.setStatistic(JsonHelper.parseFromJson(document.getJsonValue(), StatisticDto.class));
        return bookMarkDto;
    }

}
