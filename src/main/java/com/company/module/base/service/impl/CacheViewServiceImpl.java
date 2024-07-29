package com.company.module.base.service.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.company.module.base.service.CacheViewService;
import com.company.module.repository.ApplicationRepository;
import com.company.module.utils.ConvertUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.module.base.repository.ApplicationViewRepository;

import lombok.extern.slf4j.Slf4j;
import com.company.module.base.view.ApplicationView;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheViewServiceImpl implements CacheViewService {

    private final ApplicationViewRepository applicationViewRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public void loadCache() {
        log.info("load cache");
        applicationViewRepository.deleteAll();
        applicationViewRepository.saveAll(applicationRepository.findAll().stream()
                .map(ConvertUtils::convertView).collect(Collectors.toList()));
    }

    @Override
    public Map<String, ApplicationView> getApplicationViews() {
        try {
            Iterable<ApplicationView> tmpRedis = applicationViewRepository.findAll();
            log.debug("getApplicationView");
            return StreamSupport.stream(tmpRedis.spliterator(), false)
                    .filter(x -> !ObjectUtils.isEmpty(x.getAppName()))
                    .peek(a -> a.setStatus(1))
                    .collect(Collectors.toMap(ApplicationView::getAppName, Function.identity()));
        } catch (Exception ex) {
            log.error("getApplicationView - Exception, cause - {} {}",
                    ex.getMessage(), ex.getCause().getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public ApplicationView getApplicationView(Long id) {
        return applicationViewRepository.findById(id).orElse(null);
    }
}
