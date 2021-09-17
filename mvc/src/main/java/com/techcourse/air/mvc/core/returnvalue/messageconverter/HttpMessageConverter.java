package com.techcourse.air.mvc.core.returnvalue.messageconverter;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface HttpMessageConverter<T> {
    boolean canWrite(Class<?> clazz);

    void write(T t, HttpServletResponse response) throws IOException;
}
