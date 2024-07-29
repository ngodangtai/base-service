package com.company.module.base.repository;

import com.company.module.base.dto.DocumentDto;
import com.company.module.common.EDocumentSource;

public interface DocRepository {
    EDocumentSource getDocumentSource();
    boolean checkConnect();
    String saveDocument(DocumentDto documentDto);
    DocumentDto getDocument(String documentId);
    boolean deleteDocument(String documentId);
}
