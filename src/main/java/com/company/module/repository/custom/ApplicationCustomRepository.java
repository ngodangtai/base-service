package com.company.module.repository.custom;

import com.company.module.base.dto.PageView;
import com.company.module.entity.ApplicationEntity;
import com.company.module.request.ApplicationSearchReq;
import com.company.module.request.BaseSearchReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;

@Slf4j
@Repository
public class ApplicationCustomRepository extends BaseCustomRepository<ApplicationEntity> {

    @Override
    public PageView<ApplicationEntity> search(BaseSearchReq request) {
        ApplicationSearchReq searchRequest = (ApplicationSearchReq) request;
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> mapParams = newMapParameters();

        sql.append(" SELECT * FROM BASE_APPLICATION WHERE 1=1 ");
        if (!ObjectUtils.isEmpty(searchRequest.getAppIds())) {
            sql.append(" AND APPID IN :appIds");
            mapParams.put("appIds", searchRequest.getAppIds());
        }
        if (!ObjectUtils.isEmpty(searchRequest.getAppName())) {
            sql.append(" AND APPNAME = :appName");
            mapParams.put("appName", searchRequest.getAppName());
        }
        sql.append(" ORDER BY ");
        if (ObjectUtils.isEmpty(request.getOrderBy())) {
            sql.append("APPID");
        } else {
            sql.append(request.getOrderBy());
        }
        return super.getRecord(sql.toString(), mapParams, request, ApplicationEntity.class);
    }
}
