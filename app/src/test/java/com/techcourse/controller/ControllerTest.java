package com.techcourse.controller;

import nextstep.mvc.mapper.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;

public class ControllerTest {
    protected AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
    }
}
