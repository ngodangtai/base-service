package com.company.module.common;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum EDocumentSource {
    S3("S3"),
    ECM("ECM"),
    LOCAL("LOCAL"),
    ;

    private final String value;
}
