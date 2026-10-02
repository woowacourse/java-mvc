package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class FakeRequestMapping implements RequestMapping {

    private final String value;
    private final RequestMethod[] method;

    FakeRequestMapping(final String value, final RequestMethod[] method) {
        this.value = value;
        this.method = method;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public RequestMethod[] method() {
        return method;
    }

    @Override
    public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return RequestMapping.class;
    }
}
