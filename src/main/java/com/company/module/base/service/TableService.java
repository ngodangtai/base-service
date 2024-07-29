package com.company.module.base.service;

import com.company.module.base.dto.FieldInputDto;
import com.company.module.base.dto.TableDto;

import java.util.List;

public interface TableService<T> {
    List<FieldInputDto> getTemplate();
    T validate(TableDto tableDto);
    void save(TableDto tableDto);
    void delete(TableDto tableDto);
    T get(Long id);
}
