package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;

public class ControllerInstanceCreationFailedException extends RuntimeException {

    public ControllerInstanceCreationFailedException(Constructor<?> constructor, Throwable cause) {
        super(getMessage(constructor), cause);
    }

    private static String getMessage(Constructor<?> constructor) {
        return String.format("컨트롤러 인스턴스를 만들 수 없습니다. - constructor: %s", constructor.getName());
    }
}
