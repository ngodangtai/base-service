package com.company.module.base.service.impl;

import com.company.module.base.repository.IndexRepository;
import com.company.module.base.service.IndexationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexationServiceImpl implements IndexationService {

    private final IndexRepository indexRepository;

}
