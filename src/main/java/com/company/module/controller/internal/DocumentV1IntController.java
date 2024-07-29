package com.company.module.controller.internal;

import com.company.module.base.dto.DocumentDto;
import com.company.module.common.EDocumentSource;
import com.company.module.controller.BaseController;
import com.company.module.jwt.BaseJWT;
import com.company.module.manager.DocumentManager;
import com.company.module.request.DocumentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/i/v1/document")
@RequiredArgsConstructor
public class DocumentV1IntController extends BaseController {

    private final DocumentManager documentManager;

    @PostMapping(value = "/upload")
    public ResponseEntity<?> upload(BaseJWT jwt, @RequestBody DocumentDto documentDto) {
        super.validateInternalAccess(jwt);
        DocumentReq documentReq = DocumentReq.builder().documentDto(documentDto).build();
        return documentManager.upload(documentReq);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<?> download(BaseJWT jwt, @RequestParam String documentId) {
        super.validateInternalAccess(jwt);
        DocumentDto documentDto = DocumentDto.builder().source(EDocumentSource.S3).documentId(documentId).build();
        DocumentReq documentReq = DocumentReq.builder().documentDto(documentDto).build();
        return documentManager.download(documentReq);
    }

    @DeleteMapping(value = "/remove")
    public ResponseEntity<?> remove(BaseJWT jwt, @RequestParam String documentId) {
        super.validateInternalAccess(jwt);
        DocumentDto documentDto = DocumentDto.builder().source(EDocumentSource.S3).documentId(documentId).build();
        DocumentReq documentReq = DocumentReq.builder().documentDto(documentDto).build();
        return documentManager.remove(documentReq);
    }

}
