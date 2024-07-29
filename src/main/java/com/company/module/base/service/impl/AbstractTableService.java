package com.company.module.base.service.impl;

import com.company.module.base.dto.FieldInputDto;
import com.company.module.base.dto.TableDto;
import com.company.module.base.service.Description;
import com.company.module.base.service.TableService;
import com.company.module.exception.BaseException;
import com.company.module.exception.ErrorCode;
import com.company.module.utils.MapperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTableService<T> implements TableService<T> {

    protected abstract JpaRepository<T, Long> getRepository();

    protected List<FieldInputDto> getTemplateByClass(Class<T> clazz){
        return Arrays.stream(clazz.getDeclaredFields())
                .map(f -> {
                    Description description = f.getAnnotation(Description.class);
                    return FieldInputDto.init(f.getName(), description);
                })
                .collect(Collectors.toList());
    }

    protected T getBody(TableDto tableDto, Class<T> clazz) {
        if (ObjectUtils.isEmpty(tableDto.getUserName()))
            throw new BaseException(null, ErrorCode.INVALID_PARAMS, new StringBuilder("UserName is not empty"));
        if (ObjectUtils.isEmpty(tableDto.getData()))
            throw new BaseException(null, ErrorCode.INVALID_PARAMS, new StringBuilder("Data is not empty"));
        T t = MapperUtils.readValue(tableDto.getData(), clazz);
        if (t == null) throw new BaseException(null, ErrorCode.INVALID_PARAMS, new StringBuilder("Data is invalid"));
        else return t;
    }

    @Override
    public void save(TableDto tableDto) {
        getRepository().save(validate(tableDto));
    }

    @Override
    public void delete(TableDto tableDto) {
        if (tableDto != null && tableDto.getId() != null) {
            getRepository().deleteById(tableDto.getId());
        }
    }

    @Override
    public T get(Long id) {
        return getRepository().findById(id).orElse(null);
    }
}
