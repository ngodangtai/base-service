package com.company.module.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageView<T> implements Serializable {

	private Integer totalPage;
	private List<T> content;
	private Integer totalRow;

	public PageView<T> convert(List<T> page, int totalPage, int totalRow) {
		this.content = page;
		this.totalPage = totalPage;
		this.totalRow = totalRow;
		return this;
	}
}
