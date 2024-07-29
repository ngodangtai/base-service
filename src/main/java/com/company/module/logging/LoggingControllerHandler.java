package com.company.module.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LoggingControllerHandler {

	@Autowired
	private AutoLoggingService autoLoggingService;

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
	}

	@Pointcut("execution(public * *(..))")
	protected void allMethod() {
	}

	// before -> Any resource annotated with @Controller annotation
	// and all method and function taking HttpServletRequest as first parameter
	@Before("controller() && allMethod() && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void logBefore(JoinPoint joinPoint) {
		autoLoggingService.writeAccessLog(joinPoint);
	}

	// After -> All method within resource annotated with @Controller annotation
	// and return a value
	@AfterReturning(pointcut = "controller() && allMethod()", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {
		if (result instanceof Map || result instanceof List) {
			log.debug("Return Map or List or PageDataDto value");
		} else {
			log.debug("traceId");
		}
	}

	// After -> Any method within resource annotated with @Controller annotation
	// throws an exception ...Log it
	@AfterThrowing(pointcut = "controller() && allMethod()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
		log.error("An exception has been thrown in {} Cause: {}", joinPoint.getSignature().getName(), exception.getCause());
		log.error(exception.getMessage(), exception);
	}
}