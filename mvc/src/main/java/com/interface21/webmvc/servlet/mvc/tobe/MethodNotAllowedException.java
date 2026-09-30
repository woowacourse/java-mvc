package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;

import java.util.Set;
import java.util.stream.Collectors;

public class MethodNotAllowedException extends RuntimeException {

    private final String allowHeader;

    public MethodNotAllowedException(String path, String method, Set<RequestMethod> allowedMethods) {
        super("HTTP method " + method + " is not supported for " + path);
        this.allowHeader = allowedMethods.stream()
                .sorted()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    public String getAllowHeader() {
        return allowHeader;
    }
}
