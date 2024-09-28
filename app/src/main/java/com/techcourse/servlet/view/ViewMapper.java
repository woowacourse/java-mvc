package com.techcourse.servlet.view;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public enum ViewMapper {

    AUTHORIZATION(401, "401.jsp"),
    NOT_FOUND(404, "404.jsp"),
    INTERNAL_SERVER_ERROR(500, "500.jsp"),
    ;

    private final int statusCode;
    private final String viewName;

    ViewMapper(int statusCode, String viewName) {
        this.statusCode = statusCode;
        this.viewName = viewName;
    }

    public static String findViewNameWithStatusCode(int statusCode) {
        return Stream.of(values())
                .filter(view -> view.statusCode == statusCode)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(statusCode + "에 해당하는 뷰가 없습니다."))
                .viewName;
    }
}
