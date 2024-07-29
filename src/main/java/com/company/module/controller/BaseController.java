package com.company.module.controller;

import com.company.module.jwt.BaseJWT;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected void validateExternalAccess(BaseJWT jwt) {

    }

    protected void validateInternalAccess(BaseJWT jwt) {

    }

    protected void validateBackAccess(BaseJWT jwt) {

    }

    protected ResponseEntity<String> success() {
        return ResponseEntity.ok("Success");
    }
}
