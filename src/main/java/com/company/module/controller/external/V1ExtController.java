package com.company.module.controller.external;

import com.company.module.controller.BaseController;
import com.company.module.jwt.BaseJWT;
import com.company.module.manager.ProductManager;
import com.company.module.request.CacheReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/e/v1/product")
@RequiredArgsConstructor
public class V1ExtController extends BaseController {

    private final ProductManager productManager;

    @GetMapping(value = "/all",produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAllProduct(BaseJWT jwt) {
        super.validateExternalAccess(jwt);
        CacheReq cacheReq = CacheReq.builder().build();
        return productManager.getAllProduct(cacheReq);
    }

    @GetMapping(value = "/{id}",produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id,
                                        BaseJWT jwt) {
        super.validateExternalAccess(jwt);
        CacheReq cacheReq = CacheReq.builder().id(id).build();
        return productManager.getProduct(cacheReq);
    }
}
