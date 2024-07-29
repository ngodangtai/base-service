package com.company.module.event.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class AttachmentDto implements Serializable {
    private String fileName;
    private String base64;

    @Override
    public String toString() {
        return "{\"fileName\":\"" + fileName + "\",\"base64\":\"" + base64 + "\"}";
    }
}