package com.company.module.base.service.impl;

import com.company.module.base.dto.DocumentDto;
import com.company.module.base.repository.DocRepository;
import com.company.module.base.service.DocumentService;
import com.company.module.common.EDocumentSource;
import com.company.module.exception.BaseException;
import com.company.module.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final List<DocRepository> docRepositories;
    private Map<EDocumentSource, DocRepository> lookup;

    @PostConstruct
    private void init() {
        lookup = new EnumMap<>(EDocumentSource.class);
        docRepositories.forEach(d -> lookup.put(d.getDocumentSource(), d));
    }


    private DocRepository getInstance(DocumentDto documentDto) {
        if (lookup.containsKey(documentDto.getSource())) {
            return lookup.get(documentDto.getSource());
        } else {
            throw new BaseException(ErrorCode.SOURCE_INVALID, new StringBuilder("DocumentSource not found"));
        }
    }

    @Override
    public String saveDocument(DocumentDto documentDto) {
        validate(documentDto);
        validateSave(documentDto);
        return getInstance(documentDto).saveDocument(documentDto);
    }

    private void validate(DocumentDto documentDto) {
        if (ObjectUtils.isEmpty(documentDto) || ObjectUtils.isEmpty(documentDto.getSource())) {
            throw new BaseException(null, ErrorCode.INVALID_PARAMS);
        }
    }

    private void validateSave(DocumentDto documentDto) {
        if (ObjectUtils.isEmpty(documentDto.getFileName())
                || ObjectUtils.isEmpty(documentDto.getContentType())
                || ObjectUtils.isEmpty(documentDto.getFileContents())) {
            throw new BaseException(null, ErrorCode.INVALID_PARAMS);
        }
    }

    @Override
    public DocumentDto getDocument(DocumentDto documentDto) {
        validate(documentDto);
        return getInstance(documentDto).getDocument(documentDto.getDocumentId());
    }

    @Override
    public boolean deleteDocument(DocumentDto documentDto) {
        validate(documentDto);
        return getInstance(documentDto).deleteDocument(documentDto.getDocumentId());
    }
}
