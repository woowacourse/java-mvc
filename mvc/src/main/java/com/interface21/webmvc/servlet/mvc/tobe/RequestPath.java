package com.interface21.webmvc.servlet.mvc.tobe;

public class RequestPath {

    public static final String BASE_PATH = "/";

    private final String value;

    public RequestPath(String value) {
        this.value = normalize(value);
    }

    public String getValue() {
        return value;
    }

    private String normalize(String value) {
        // 비었다면 기본 경로로 지정
        if (value.isEmpty()) {
            return BASE_PATH;
        }

        if (!value.startsWith(BASE_PATH)) {
            // 앞 '/' 추가
            value = BASE_PATH + value;
        }
        if (value.length() > 1 && value.endsWith(BASE_PATH)) {
            // 뒤 '/' 제거 (루트 경로는 제외)
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }
}
