package com.company.module.base.service;

import com.company.module.base.dto.DocumentDto;

public interface DocumentService {

    String saveDocument(DocumentDto documentDto);
    DocumentDto getDocument(DocumentDto documentDto);
    boolean deleteDocument(DocumentDto documentDto);
}
