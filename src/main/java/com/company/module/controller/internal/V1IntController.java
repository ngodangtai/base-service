package com.company.module.controller.internal;

import com.company.module.controller.BaseController;
import com.company.module.manager.BizManager;
import com.company.module.manager.SearchManager;
import com.company.module.request.ApplicationSearchReq;
import com.company.module.request.BizReq;
import com.company.module.request.SearchReq;
import com.company.module.jwt.BaseJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/i/v1/")
@RequiredArgsConstructor
public class V1IntController extends BaseController {

    private final SearchManager searchManager;
    private final BizManager bizManager;

    @GetMapping(value = "/all",produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAllProduct(BaseJWT jwt) {
        super.validateInternalAccess(jwt);
        SearchReq searchReq = SearchReq.builder().build();
        return searchManager.getAllProduct(searchReq);
    }

    @GetMapping(value = "/phone-code")
    public ResponseEntity<?> getPhoneCodes(BaseJWT jwt) {
        super.validateInternalAccess(jwt);
        SearchReq searchReq = SearchReq.builder().build();
        return searchManager.getPhoneCodes(searchReq);
    }

    @GetMapping(value = "/search-app")
    public ResponseEntity<?> searchApplication(BaseJWT jwt) {
        super.validateInternalAccess(jwt);
        ApplicationSearchReq searchReq = ApplicationSearchReq.builder()
                .appIds(Collections.singletonList(1L))
                .build();
        return searchManager.searchApplication(searchReq);
    }

    @GetMapping(value = "/rabbit",produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public void publishRabbit(BaseJWT jwt) {
        super.validateInternalAccess(jwt);
        BizReq bizReq = BizReq.builder().build();
        bizManager.publishRabbit(bizReq);
    }

        @GetMapping(value = "/kafka",produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public void publishKafka(BaseJWT jwt) {
        super.validateInternalAccess(jwt);
        BizReq bizReq = BizReq.builder().build();
        bizManager.publishKafka(bizReq);
    }

    @GetMapping(value = "/redis",produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public void publishRedis(BaseJWT jwt) {
        super.validateInternalAccess(jwt);
        BizReq bizReq = BizReq.builder().build();
        bizManager.publishRedis(bizReq);
    }
}
