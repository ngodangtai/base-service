package com.company.module.base.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ApiModel
@AllArgsConstructor
public class TableDto implements Serializable {
	private String userName;
	private Long id;
	private String data;
}
