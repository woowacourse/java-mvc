package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import com.interface21.web.bind.annotation.RequestMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        final var annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        handlerMapping = annotationHandlerMapping;
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void supportsAllMethodsWhenMethodIsOmitted(RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/all-test");
        when(request.getMethod()).thenReturn(method.name());
        when(request.getAttribute("id")).thenReturn("gugu");

        final var execution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(execution.handle(request, response).getObject("id")).isEqualTo("gugu");
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class, names = {"PUT", "DELETE"})
    void mapsSameUrlByHttpMethod(RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn(method.name());

        final var execution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(execution.handle(request, response).getObject("method")).isEqualTo(method.name());
    }

    @Test
    void returnsNullForUnmappedRequest() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");
        assertThat(handlerMapping.getHandler(request)).isNull();

        when(request.getRequestURI()).thenReturn("/missing");
        when(request.getMethod()).thenReturn("GET");
        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
