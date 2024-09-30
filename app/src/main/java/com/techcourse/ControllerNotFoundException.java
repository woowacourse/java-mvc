package com.techcourse;

public class ControllerNotFoundException extends RuntimeException {

    public ControllerNotFoundException(String requestURI) {
        super(getMessage(requestURI));
    }

    private static String getMessage(String requestURI) {
        return String.format("요청에 대한 컨트롤러를 찾을 수 없습니다. - request: %s", requestURI);
    }
}
