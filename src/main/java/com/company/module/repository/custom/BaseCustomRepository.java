package com.company.module.repository.custom;

import com.company.module.base.dto.PageView;
import com.company.module.request.BaseSearchReq;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;

@Slf4j
public abstract class BaseCustomRepository<T> {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    protected EntityManager entityManager;

    private static final Integer PAGE_NUMBER_DEFAULT = 1;
    private static final Integer PAGE_SIZE_DEFAULT = 50;

    public abstract PageView<T> search(BaseSearchReq request);

    protected HashMap<String, Object> newMapParameters() {
        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    protected PageView<T> getRecord(String sqlString, HashMap<String, Object> mapParams, BaseSearchReq request,
                                     Class<T> resultClass) {
        Integer pageNumber = ObjectUtils.isEmpty(request.getPageNumber()) ? PAGE_NUMBER_DEFAULT : request.getPageNumber();
        Integer pageSize = ObjectUtils.isEmpty(request.getPageSize()) ? PAGE_SIZE_DEFAULT : request.getPageSize();
        try {
            Query query = entityManager.createNativeQuery(sqlString, resultClass);
            setParametersByMap(query, mapParams);
            int totalRow = query.getResultList().size();
            Integer offset = (pageNumber - 1) * pageSize;
            this.pagingQuery(query, offset, pageSize);
            PageView<T> pageview = new PageView<>();
            pageview.setContent(query.getResultList());
            pageview.setTotalRow(totalRow);
            pageview.setTotalPage(this.getTotalPage(totalRow, pageSize));
            return pageview;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private void setParametersByMap(final Query query, final HashMap<String, Object> mapParams) {
        mapParams.keySet().forEach(param -> {
            Object value = mapParams.get(param);
            query.setParameter(param, value);
        });
    }

    private void pagingQuery(final Query query, Integer offset, Integer pageSize) {
        if (offset != null && pageSize != null && pageSize > 0 && offset >= 0) {
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
        }
    }

    private int getTotalPage(int totalRecord, Integer pageSize) {
        int result = 1;
        if (pageSize != null && pageSize > 0) {
            result = (totalRecord % pageSize) == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        }
        return result;
    }
}
