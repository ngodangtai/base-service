package com.company.module.utils;

import com.company.module.base.view.ApplicationView;
import com.company.module.dto.ApplicationDto;
import com.company.module.entity.ApplicationEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtils {

    public static ApplicationDto convertDto(ApplicationEntity applicationEntity) {
        return MapperUtils.copyProperties(applicationEntity, ApplicationDto.class);
    }

    public static ApplicationView convertView(ApplicationEntity applicationEntity) {
        return MapperUtils.copyProperties(applicationEntity, ApplicationView.class);
    }
}
