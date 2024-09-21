package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

public class FailHandlerExecuteException extends RuntimeException {

    public FailHandlerExecuteException(final Method method, final Exception cause) {
        super(String.format("%s에 대해 실행 실패했습니다.", method), cause);
    }
}
