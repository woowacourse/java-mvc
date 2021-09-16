package com.techcourse.air.mvc.core.returnvalue.messageconverter;

import java.io.IOException;

import com.techcourse.air.core.annotation.Order;
import com.techcourse.air.mvc.web.support.MediaType;

import jakarta.servlet.http.HttpServletResponse;

@Order(1)
public class StringHttpMessageConverter extends AbstractHttpMessageConverter<String> {

    @Override
    public boolean canWrite(Class<?> clazz) {
        return String.class == clazz;
    }

    @Override
    protected void writeInternal(String string, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(string);
    }
}
