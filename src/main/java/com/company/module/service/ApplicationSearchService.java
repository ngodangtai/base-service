package com.company.module.service;

import com.company.module.base.dto.PageView;
import com.company.module.client.dto.CountryDto;
import com.company.module.dto.ApplicationDto;
import com.company.module.request.ApplicationSearchReq;

import java.util.List;

public interface ApplicationSearchService {

    List<ApplicationDto> getApplications();
    PageView<ApplicationDto> searchApplication(ApplicationSearchReq request);
    List<CountryDto> getPhoneCodes();
}
