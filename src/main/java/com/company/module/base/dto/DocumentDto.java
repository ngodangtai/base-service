package com.company.module.base.dto;

import com.company.module.common.EDocumentSource;
import com.company.module.common.EDocumentFolder;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Map;

@Data
@Builder
@ApiModel
@AllArgsConstructor
public class DocumentDto {

	private EDocumentSource source;
	private String documentId;
	private EDocumentFolder folder;
	private String fileName;
	private String contentType;
	private Map<String, String> metaData;
	private String fileContents;
	private long contentLength;
}
