package com.company.module.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@Builder
@ApiModel
@AllArgsConstructor
public class CountryDto {
    private String countryCode;
    private String countryName;
    private String phoneCode;
}
