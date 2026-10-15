package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodNotAllowedException extends RuntimeException {

    private final Set<RequestMethod> allowedMethods;

    public MethodNotAllowedException(final String method, final String requestURI,
                                     final Set<RequestMethod> allowedMethods) {
        super("지원하지 않는 HTTP 메서드입니다: " + method + " " + requestURI + ", 지원 메서드: " + allowedMethods);
        this.allowedMethods = Set.copyOf(allowedMethods);
    }

    // 405 응답에 필수로 담아야 하는 Allow 헤더 값을 만든다
    public String getAllowHeaderValue() {
        return allowedMethods.stream()
                .map(RequestMethod::name)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    public Set<RequestMethod> getAllowedMethods() {
        return allowedMethods;
    }
}
