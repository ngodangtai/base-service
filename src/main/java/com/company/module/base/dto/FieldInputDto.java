package com.company.module.base.dto;

import com.company.module.base.service.Description;
import com.company.module.common.EFieldType;
import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@Builder
@ApiModel
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FieldInputDto {
    private String fieldCode;
    private String fieldName;
    private EFieldType fieldType;
    private boolean required;
    private int maxLength;
    private int minLength;
    private boolean isUnique;
    private String defaultValue;
    private String pattern;
    private Object value;

    public static FieldInputDto init(String fieldCode) {
        return FieldInputDto.builder()
                .fieldCode(fieldCode)
                .fieldType(EFieldType.INPUT)
                .maxLength(Integer.MAX_VALUE)
                .minLength(0)
                .build();
    }

    public static FieldInputDto init(String fieldCode, Description description) {
        if (description == null)
            return FieldInputDto.init(fieldCode);
        else
            return FieldInputDto.builder()
                .fieldCode(fieldCode)
                .fieldName(description.value())
                .fieldType(description.type())
                .required(description.required())
                .maxLength(description.maxLength())
                .minLength(description.minLength())
                .isUnique(description.isUnique())
                .defaultValue(description.defaultValue())
                .pattern(description.pattern())
                .build();
    }
}
