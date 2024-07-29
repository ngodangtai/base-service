package com.company.module.base.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.company.module.base.dto.DocumentDto;
import com.company.module.common.EDocumentFolder;
import com.company.module.common.EDocumentSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Slf4j
@Repository
@RequiredArgsConstructor
public class S3Repository implements DocRepository {

    private final AmazonS3 amazonS3;

    @Value("${aws.bucket-public}")
    private String bucketPublic;

    @Override
    public EDocumentSource getDocumentSource() {
        return EDocumentSource.S3;
    }

    @Override
    public boolean checkConnect() {
        return amazonS3.doesBucketExistV2(bucketPublic);
    }

    @Override
    public String saveDocument(DocumentDto documentDto) {
        String documentId = getDocumentId(documentDto);
        log.info("Put object documentId {} - start", documentId);
        try {
            byte[] fileContents = Base64.getEncoder().encode(documentDto.getFileContents().getBytes());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileContents.length);
            metadata.setContentType(documentDto.getContentType());
            metadata.setUserMetadata(documentDto.getMetaData());
            amazonS3.putObject(bucketPublic, documentId, new ByteArrayInputStream(fileContents), metadata);
            log.info("Put object success documentId: {}", documentId);
            return documentId;
        } catch (Exception e) {
            log.error("Put object error documentId: {}, error: {} ", documentId, e.getMessage(), e);
            throw e;
        }
    }

    private String getDocumentId(DocumentDto documentDto) {
        if (!ObjectUtils.isEmpty(documentDto.getFolder()))
            return documentDto.getFolder().getValue() + documentDto.getFileName();
        else
            return EDocumentFolder.DOCUMENT.getValue() + documentDto.getFileName();
    }

    @Override
    public DocumentDto getDocument(String documentId) {
        log.info("Get object documentId {} - start", documentId);
        try {
            S3Object s3Object = amazonS3.getObject(bucketPublic, documentId);

            return DocumentDto.builder()
                    .source(EDocumentSource.S3)
                    .documentId(documentId)
                    .contentType(s3Object.getObjectMetadata().getContentType())
                    .contentLength(s3Object.getObjectMetadata().getContentLength())
                    .metaData(s3Object.getObjectMetadata().getUserMetadata())
                    .fileContents(new String(Base64.getDecoder().decode(IOUtils.toByteArray(s3Object.getObjectContent()))))
                    .build();
        } catch (Exception e) {
            log.error("getDocument error documentId: {}", documentId, e);
            return null;
        }
    }

    @Override
    public boolean deleteDocument(String documentId) {
        log.info("Delete object documentId {} - start", documentId);
        try {
            amazonS3.deleteObject(bucketPublic, documentId);
            return true;
        } catch (Exception e) {
            log.error("deleteDocument error documentId: {}", documentId, e);
            throw e;
        }
    }
}