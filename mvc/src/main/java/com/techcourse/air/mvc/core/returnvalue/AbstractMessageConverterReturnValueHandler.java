package com.techcourse.air.mvc.core.returnvalue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.techcourse.air.mvc.core.returnvalue.messageconverter.HttpMessageConverter;
import com.techcourse.air.mvc.core.returnvalue.messageconverter.MessageConverterComparator;
import com.techcourse.air.mvc.core.returnvalue.messageconverter.ObjectHttpMessageConverter;
import com.techcourse.air.mvc.core.returnvalue.messageconverter.StringHttpMessageConverter;

import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractMessageConverterReturnValueHandler implements ReturnValueHandler {

    protected List<HttpMessageConverter<?>> messageConverters;

    public AbstractMessageConverterReturnValueHandler() {
        this.messageConverters = new ArrayList<>();
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new ObjectHttpMessageConverter());
        Collections.sort(messageConverters, new MessageConverterComparator());
    }

    protected <T> void writeWithMessageConverter(T value, HttpServletResponse response) throws IOException {
        HttpMessageConverter<?> converter = messageConverters.stream()
                                                             .filter(mc -> mc.canWrite(value.getClass()))
                                                             .findFirst()
                                                             .orElseThrow();

        ((HttpMessageConverter) converter).write(value, response);
    }
}
