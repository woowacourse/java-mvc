package com.techcourse.controller;

import org.junit.jupiter.api.BeforeEach;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.HandlerMapping;

public abstract class ControllerTest {

    protected HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
    }
}
