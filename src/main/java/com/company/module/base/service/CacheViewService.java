package com.company.module.base.service;

import java.util.Map;

import com.company.module.base.view.ApplicationView;

public interface CacheViewService {
    void loadCache();
    Map<String, ApplicationView> getApplicationViews();
    ApplicationView getApplicationView(Long id);
}
