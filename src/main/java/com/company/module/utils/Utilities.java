package com.company.module.utils;

import com.company.module.common.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.function.Predicate;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Utilities {

    private static final ExpressionParser expressionParser = new SpelExpressionParser();

    @SafeVarargs
    public static <T> T firstSatisfied(Predicate<T> predicate, Supplier<T>... suppliers) {
        for (var supplier : suppliers) {
            var t = supplier.get();
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    public static Object extractKeyFromSpel(ProceedingJoinPoint jp, String spel) {
        var context = new StandardEvaluationContext();
        var methodSignature = (MethodSignature) jp.getSignature();
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            context.setVariable(methodSignature.getParameterNames()[i], jp.getArgs()[i]);
        }
        try {
            var expression = expressionParser.parseExpression(spel);
            return expression.getValue(context);
        } catch (Exception e) {
            log.error("Error while parsing SpEL {}", spel, e);
            return Constants.Character.EMPTY;
        }
    }
}
