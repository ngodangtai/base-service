package com.company.module.common;

import com.company.module.exception.ErrorCode;
import com.company.module.exception.BaseException;
import lombok.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Executor {
        public static final String EXECUTOR_MAIN = "mainTaskExecutor";
        public static final String EXECUTOR_ASYNC = "asyncTaskExecutor";
        public static final String EXECUTOR_SCHEDULER = "schedulerTaskExecutor";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Character {
        public static final String AMPERSAND = "&";
        public static final String ARROW = "->";
        public static final String ASTERISK = "*";
        public static final String COLON = ":";
        public static final String COMMA = ",";
        public static final String DOT = ".";
        public static final String DOUBLE_QUOTE = "\"";
        public static final String EMPTY = "";
        public static final String HYPHEN = "-";
        public static final String SEMICOLON = ";";
        public static final String SLASH = "/";
        public static final String SPACE = " ";
        public static final String SPLITTER = "|";
        public static final String UNDERSCORE = "_";
        public static final String ZERO = "0";
        public static final String PERCENTAGE = "%";
        public static final String LIKE = "%s";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Regex {
        public static final String SPECIAL_CHARACTER_STRING_JAVA_REGEX = "[\n\t\r!@#$%^&*_;\"<>?\\[\\]{}|~`=\\/'“”]";
    }

    @Getter
    @RequiredArgsConstructor
    public enum COMPARE {
        LESS_THAN("LESS_THAN"), GREATER_THAN("GREATER_THAN"), EQUALS("EQUALS"),;

        private final String value;
        private static final Map<String, COMPARE> lookupValue = new HashMap<>();
        static {
            for (COMPARE e : EnumSet.allOf(COMPARE.class)) {
                lookupValue.put(e.getValue(), e);
            }
        }

        public static COMPARE lookup(String value) {
            COMPARE ms = lookupValue.get(value.toUpperCase());
            if (ms == null) {
                throw new BaseException(ErrorCode.DATA_NOT_FOUND,
                        new StringBuilder("COMPARE by value:").append(value).append(" not found"));
            } else {
                return ms;
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum ACTION {
        BUY(1, "BUY"), SELL(2, "SELL");

        private final Integer id;
        private final String code;
    }

	@Getter
    @RequiredArgsConstructor
    public enum CUSTOMER_TYPE {
		CORPORATE("CORPORATE"), PERSONAL("PERSONAL");

		public final String type;
    }

	@Getter
    @RequiredArgsConstructor
    public enum RATE_TYPE {
		REINVEST("REINVEST"), INVEST("INVEST");

		public final String type;
    }

}
