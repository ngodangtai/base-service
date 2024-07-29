package com.company.module.request;

import com.company.module.base.dto.DocumentDto;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class DocumentReq extends BaseReq {
    private DocumentDto documentDto;
}
