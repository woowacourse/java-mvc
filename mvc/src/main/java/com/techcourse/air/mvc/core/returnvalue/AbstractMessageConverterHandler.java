package com.techcourse.air.mvc.core.returnvalue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.techcourse.air.mvc.core.messageconverter.HttpMessageConverter;
import com.techcourse.air.mvc.core.messageconverter.ObjectHttpMessageConverter;
import com.techcourse.air.mvc.core.messageconverter.StringHttpMessageConverter;

import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractMessageConverterHandler implements ReturnValueHandler {

    protected List<HttpMessageConverter<?>> messageConverters;

    public AbstractMessageConverterHandler() {
        this.messageConverters = new ArrayList<>();
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new ObjectHttpMessageConverter());
    }

    protected <T> void writeWithMessageConverter(T value, HttpServletResponse response) throws IOException {
        HttpMessageConverter<?> converter = messageConverters.stream()
                                                             .filter(mc -> mc.canWrite(value.getClass()))
                                                             .findFirst()
                                                             .orElseThrow();

        ((HttpMessageConverter) converter).write(value, response);
    }
}
