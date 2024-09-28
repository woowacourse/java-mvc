package com.interface21.webmvc.servlet.mvc.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerAdapterTest {
    private AnnotationHandlerMapping handlerMapping;
    private AnnotationHandlerAdapter annotationHandlerAdapter;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples.success");
        handlerMapping.init();
        annotationHandlerAdapter = new AnnotationHandlerAdapter();
    }

    @Test
    void handleTest() throws Exception {
        final var request = new MockHttpServletRequest("GET", "/get-test");
        request.setAttribute("id", "gugu");
        final var response = new MockHttpServletResponse();

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = annotationHandlerAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void canHandleTrueTest() {
        final var request = new MockHttpServletRequest("GET", "/get-test");
        request.setAttribute("id", "gugu");
        final var response = new MockHttpServletResponse();

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(annotationHandlerAdapter.canHandle(handlerExecution)).isTrue();
    }
}
