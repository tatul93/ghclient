package com.webfontaine.ghclient.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * BookMark domain class
 */

@Document(collection = "bookmarks")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookMarkDocument {

    private String cacheId;

    @Id
    @Indexed(unique = true)
    private String bookMarkId;

    private String jsonValue;

    private LocalDateTime createdDate;


}
