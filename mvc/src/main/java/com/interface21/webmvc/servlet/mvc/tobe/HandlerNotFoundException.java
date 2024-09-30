package com.interface21.webmvc.servlet.mvc.tobe;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(String requestURI) {
        super(getMessage(requestURI));
    }

    private static String getMessage(String requestURI) {
        return String.format("해당 요청에 대한 핸들러를 찾을 수 없습니다. - requestURI: %s", requestURI);
    }
}
