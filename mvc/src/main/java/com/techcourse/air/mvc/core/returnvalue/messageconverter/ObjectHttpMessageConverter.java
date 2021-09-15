package com.techcourse.air.mvc.core.returnvalue.messageconverter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcourse.air.mvc.web.support.MediaType;

import jakarta.servlet.http.HttpServletResponse;

public class ObjectHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    @Override
    public boolean canWrite(Class<?> clazz) {
        return clazz != null;
    }

    @Override
    protected void writeInternal(Object data, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}
