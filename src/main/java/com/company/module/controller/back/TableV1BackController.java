package com.company.module.controller.back;

import com.company.module.base.dto.TableDto;
import com.company.module.common.ETableName;
import com.company.module.controller.BaseController;
import com.company.module.jwt.BaseJWT;
import com.company.module.manager.TableManager;
import com.company.module.request.TableReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/b/v1/table")
@RequiredArgsConstructor
public class TableV1BackController extends BaseController {

    private final TableManager tableManager;

    @GetMapping(value = "/template", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getTemplate(@RequestParam ETableName tableName,
                                         BaseJWT jwt) {
        validateBackAccess(jwt);
        TableReq tableReq = TableReq.builder()
                .tableName(tableName)
                .build();
        return tableManager.getTemplate(tableReq);
    }

    @PostMapping(value = "/validate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> validate(@RequestParam ETableName tableName,
                                      @RequestBody TableDto tableDto,
                                      BaseJWT jwt) {
        validateBackAccess(jwt);
        TableReq tableReq = TableReq.builder()
                .tableName(tableName)
                .tableDto(tableDto)
                .build();
        return tableManager.validate(tableReq);
    }

    @PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> save(@RequestParam ETableName tableName,
                                  @RequestBody TableDto tableDto,
                                  BaseJWT jwt) {
        validateBackAccess(jwt);
        TableReq tableReq = TableReq.builder()
                .tableName(tableName)
                .tableDto(tableDto)
                .build();
        tableManager.save(tableReq);
        return success();
    }

    @PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@RequestParam ETableName tableName,
                                    @RequestBody TableDto tableDto,
                                    BaseJWT jwt) {
        validateBackAccess(jwt);
        TableReq tableReq = TableReq.builder()
                .tableName(tableName)
                .tableDto(tableDto)
                .build();
        tableManager.delete(tableReq);
        return success();
    }

    @GetMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@RequestParam ETableName tableName,
                                 @RequestParam Long id,
                                 BaseJWT jwt) {
        validateBackAccess(jwt);
        TableReq tableReq = TableReq.builder()
                .tableName(tableName)
                .tableDto(TableDto.builder().id(id).build())
                .build();
        return tableManager.get(tableReq);
    }
}
