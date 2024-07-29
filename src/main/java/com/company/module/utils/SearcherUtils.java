package com.company.module.utils;

import com.company.module.dto.ApplicationDto;
import com.company.module.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SearcherUtils {

    private final ApplicationRepository applicationRepository;

    public ApplicationDto getApplicationDto(String appName) {
        return applicationRepository.findByAppName(appName).map(ConvertUtils::convertDto).orElse(null);
    }

}
