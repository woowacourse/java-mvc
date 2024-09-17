package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;

public class FailInstanceCreateException extends RuntimeException {
    public FailInstanceCreateException(final Constructor<?> constructor, final Exception cause) {
        super(String.format("%s에 대한 생성을 실패했습니다.", constructor), cause);
    }
}
