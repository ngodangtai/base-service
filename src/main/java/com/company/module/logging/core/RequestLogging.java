package com.company.module.logging.core;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Objects;

@Getter
@ApiModel
public class RequestLogging {
    private String path;
    private String method;

    public static RequestLoggingBuilder builder() {
        return new RequestLoggingBuilder();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RequestLogging other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$path = this.getPath();
                Object other$path = other.getPath();
                if (this$path == null) {
                    if (other$path != null) {
                        return false;
                    }
                } else if (!this$path.equals(other$path)) {
                    return false;
                }

                return Objects.equals(this.getMethod(), other.getMethod());
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof RequestLogging;
    }

    public int hashCode() {
        int result = 1;
        Object $path = this.getPath();
        result = result * 59 + ($path == null ? 43 : $path.hashCode());
        Object $method = this.getMethod();
        result = result * 59 + ($method == null ? 43 : $method.hashCode());
        return result;
    }

    public String toString() {
        return "RequestLogging(path=" + this.getPath() + ", method=" + this.getMethod() + ")";
    }

    public RequestLogging() {
    }

    public RequestLogging(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public static class RequestLoggingBuilder {
        private String path;
        private String method;

        RequestLoggingBuilder() {
        }

        public RequestLoggingBuilder path(String path) {
            this.path = path;
            return this;
        }

        public RequestLoggingBuilder method(String method) {
            this.method = method;
            return this;
        }

        public RequestLogging build() {
            return new RequestLogging(this.path, this.method);
        }

        public String toString() {
            return "RequestLogging.RequestLoggingBuilder(path=" + this.path + ", method=" + this.method + ")";
        }
    }
}
