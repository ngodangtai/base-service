package com.company.module.manager;

import com.company.module.base.service.TableFactory;
import com.company.module.base.service.TableService;
import com.company.module.request.TableReq;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableManager extends BaseManager {

    private final TableFactory tableFactory;

    protected TableService<?> getTableService(@NonNull TableReq tableReq) {
        return tableFactory.getInstance(tableReq.getTableName());
    }

    public ResponseEntity<?> getTemplate(@NonNull TableReq tableReq) {
        return ResponseEntity.ok(getTableService(tableReq).getTemplate());
    }

    public ResponseEntity<?> validate(@NonNull TableReq tableReq) {
        return ResponseEntity.ok(getTableService(tableReq).validate(tableReq.getTableDto()));
    }

    public void save(@NonNull TableReq tableReq) {
        getTableService(tableReq).save(tableReq.getTableDto());
    }

    public void delete(@NonNull TableReq tableReq) {
        getTableService(tableReq).delete(tableReq.getTableDto());
    }

    public ResponseEntity<?> get(@NonNull TableReq tableReq) {
        return ResponseEntity.ok(getTableService(tableReq).get(tableReq.getTableDto().getId()));
    }
}
