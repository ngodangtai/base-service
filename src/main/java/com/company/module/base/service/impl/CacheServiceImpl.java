package com.company.module.base.service.impl;

import com.company.module.base.service.CacheService;
import com.company.module.base.repository.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final CacheRepository cacheRepository;

}
