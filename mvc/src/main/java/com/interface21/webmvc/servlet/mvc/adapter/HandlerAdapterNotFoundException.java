package com.interface21.webmvc.servlet.mvc.adapter;

public class HandlerAdapterNotFoundException extends RuntimeException {

    public HandlerAdapterNotFoundException(Object handler) {
        super(getMessage(handler));
    }

    private static String getMessage(Object handler) {
        return String.format("해당 핸들러에 대한 핸들러 어댑터를 찾을 수 없습니다. - handler: %s", handler.getClass().getName());
    }
}
