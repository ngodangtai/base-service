package com.company.module.manager;

import com.company.module.request.ApplicationSearchReq;
import com.company.module.request.SearchReq;
import com.company.module.service.ApplicationSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchManager extends BaseManager {

    private final ApplicationSearchService applicationSearchService;

    public ResponseEntity<?> getAllProduct(SearchReq searchReq) {
        return ResponseEntity.ok(applicationSearchService.getApplications());
    }

    public ResponseEntity<?> getPhoneCodes(SearchReq searchReq) {
        return ResponseEntity.ok(applicationSearchService.getPhoneCodes());
    }

    public ResponseEntity<?> searchApplication(ApplicationSearchReq searchReq) {
        return ResponseEntity.ok(applicationSearchService.searchApplication(searchReq));
    }
}
