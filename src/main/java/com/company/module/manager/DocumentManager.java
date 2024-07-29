package com.company.module.manager;

import com.company.module.base.service.DocumentService;
import com.company.module.request.DocumentReq;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentManager extends BaseManager {

    private final DocumentService documentService;

    public ResponseEntity<?> upload(@NonNull DocumentReq documentReq) {
        return ResponseEntity.ok(documentService.saveDocument(documentReq.getDocumentDto()));
    }

    public ResponseEntity<?> download(@NonNull DocumentReq documentReq) {
        return ResponseEntity.ok(documentService.getDocument(documentReq.getDocumentDto()));
    }

    public ResponseEntity<?> remove(@NonNull DocumentReq documentReq) {
        return ResponseEntity.ok(documentService.deleteDocument(documentReq.getDocumentDto()));
    }
}
