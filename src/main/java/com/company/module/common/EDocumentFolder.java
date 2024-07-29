package com.company.module.common;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum EDocumentFolder {
    AVATAR("avatar/"),
    IDENTITY_FRONT("identity/front/"),
    IDENTITY_BACK("identity/back/"),
    IMAGE("image/"),
    VIDEO("video/"),
    DOCUMENT("document/");

    private final String value;
}
