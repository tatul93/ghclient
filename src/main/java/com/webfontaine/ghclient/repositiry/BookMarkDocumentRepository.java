package com.webfontaine.ghclient.repositiry;

import com.webfontaine.ghclient.domain.BookMarkDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The Repository interface for BookMarkDocument
 */
public interface BookMarkDocumentRepository extends MongoRepository<BookMarkDocument, String> {
}
