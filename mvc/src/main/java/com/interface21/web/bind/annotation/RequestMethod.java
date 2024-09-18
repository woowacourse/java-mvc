package com.interface21.web.bind.annotation;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getMethod(String name) {
        return Arrays.stream(RequestMethod.values())
                .filter(method -> StringUtils.equalsIgnoreCase(method.name(), name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("name에 대응하는 RequestMethod를 찾을 수 없습니다. name = " + name));
    }
}
