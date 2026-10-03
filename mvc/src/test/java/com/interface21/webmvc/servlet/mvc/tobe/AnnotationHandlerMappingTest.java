package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
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

    @Test
    void supportsAllHttpMethodsWhenMethodIsOmitted() {
        for (RequestMethod method : RequestMethod.values()) {
            HttpServletRequest request = mock(HttpServletRequest.class);

            when(request.getRequestURI()).thenReturn("/all-methods");
            when(request.getMethod()).thenReturn(method.name());

            assertThat(handlerMapping.getHandler(request))
                    .as("HTTP method %s", method)
                    .isInstanceOf(HandlerExecution.class);
        }
    }

    @Test
    void duplicateMappingFailsDuringInitialization() {
        AnnotationHandlerMapping duplicateMapping = new AnnotationHandlerMapping("fixtures.duplicate");

        assertThatThrownBy(duplicateMapping::initialize)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void privateHandlerMethodCanBeInvoked() throws Exception {
        AnnotationHandlerMapping privateMapping = new AnnotationHandlerMapping("fixtures.privatehandler");
        privateMapping.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/private-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("gugu");

        HandlerExecution execution = (HandlerExecution) privateMapping.getHandler(request);

        assertThat(execution).isNotNull();
        assertThat(execution.handle(request, response).getObject("id")).isEqualTo("gugu");
    }
}
