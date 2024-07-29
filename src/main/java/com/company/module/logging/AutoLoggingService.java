package com.company.module.logging;

import org.aspectj.lang.JoinPoint;

public interface AutoLoggingService {
	void writeAccessLog(JoinPoint joinPoint);
}
