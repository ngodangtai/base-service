package com.company.module.base.view;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("ApplicationView")
@Data
public class ApplicationView implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 4844956753604356575L;

	@Id
	private Long appId;
	private String appName;
	private String appDesc;
	private Integer status;
}
