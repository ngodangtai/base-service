package com.company.module.logging;

import com.company.module.common.Constants;
import com.company.module.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerUtils {

    public static String objToStringIgnoreEx(Object prm) {
        try {
            if (prm == null) return Constants.Character.EMPTY;
            if (prm instanceof Exception ex) {
                return new StringBuilder("Exception:")
                        .append(ex.getMessage())
                        .append("-CZ:")
                        .append(ex.getCause() != null ? ex.getCause().getMessage() : "null")
                        .toString();
            } else {
                return MapperUtils.writeValueAsString(prm);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "EXCEPTION";
        }
    }
}
