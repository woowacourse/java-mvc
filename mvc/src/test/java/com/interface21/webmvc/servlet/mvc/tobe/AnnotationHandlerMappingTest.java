package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void sameUrlIsMappedByHttpMethod() throws Exception {
        final var getRequest = mock(HttpServletRequest.class);
        when(getRequest.getRequestURI()).thenReturn("/same-test");
        when(getRequest.getMethod()).thenReturn("GET");

        final var postRequest = mock(HttpServletRequest.class);
        when(postRequest.getRequestURI()).thenReturn("/same-test");
        when(postRequest.getMethod()).thenReturn("POST");

        final var response = mock(HttpServletResponse.class);
        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);

        assertThat(getHandler.handle(getRequest, response).getObject("method")).isEqualTo("GET");
        assertThat(postHandler.handle(postRequest, response).getObject("method")).isEqualTo("POST");
    }

    @Test
    void omittedMethodMatchesEverySupportedHttpMethod() {
        for (final RequestMethod method : RequestMethod.values()) {
            final var request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/any-method");
            when(request.getMethod()).thenReturn(method.name());

            assertThat(handlerMapping.getHandler(request)).isInstanceOf(HandlerExecution.class);
        }
    }
}
