package com.company.module.service.biz;

import com.company.module.dto.ApplicationDto;
import com.company.module.service.table.ApplicationSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileBizServiceImpl implements ProfileBizService {

    private final ApplicationSearchService applicationSearchService;

    @Override
    public List<ApplicationDto> getApplications() {
        return applicationSearchService.getApplications();
    }
}
