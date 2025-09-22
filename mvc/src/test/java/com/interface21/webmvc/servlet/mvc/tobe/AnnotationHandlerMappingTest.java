package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.ComponentScanner;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        ComponentScanner componentScanner = new ComponentScanner("samples");
        handlerMapping = new AnnotationHandlerMapping(componentScanner);
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        String path = "/get-test";
        when(request.getRequestURI()).thenReturn(path);
        String requestMethod = "GET";

        when(request.getMethod()).thenReturn(requestMethod);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(path, RequestMethod.GET);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        String path = "/post-test";
        when(request.getRequestURI()).thenReturn(path);
        String requestMethod = "POST";
        when(request.getMethod()).thenReturn(requestMethod);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(path, RequestMethod.valueOf(requestMethod));
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
