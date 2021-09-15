package com.techcourse.air.mvc.core.messageconverter;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractHttpMessageConverter<T> implements HttpMessageConverter<T> {

    @Override
    public void write(T t, HttpServletResponse response) throws IOException {
        writeInternal(t, response);
    }

    protected abstract void writeInternal(T t, HttpServletResponse response) throws IOException;
}
