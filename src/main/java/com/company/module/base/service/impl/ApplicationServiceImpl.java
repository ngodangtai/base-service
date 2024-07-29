package com.company.module.base.service.impl;

import com.company.module.base.dto.FieldInputDto;
import com.company.module.base.dto.TableDto;
import com.company.module.base.service.ApplicationService;
import com.company.module.common.TableNameConstants;
import com.company.module.entity.ApplicationEntity;
import com.company.module.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service(value = TableNameConstants.TABLE_APPLICATION)
public class ApplicationServiceImpl extends AbstractTableService<ApplicationEntity> implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    protected JpaRepository<ApplicationEntity, Long> getRepository() {
        return this.applicationRepository;
    }

    @Override
    public List<FieldInputDto> getTemplate() {
        return super.getTemplateByClass(ApplicationEntity.class);
    }

    @Override
    public ApplicationEntity validate(TableDto tableDto) {
        return super.getBody(tableDto, ApplicationEntity.class);
    }
}
