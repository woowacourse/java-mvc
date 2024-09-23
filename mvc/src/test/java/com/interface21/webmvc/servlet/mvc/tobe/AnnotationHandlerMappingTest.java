package com.interface21.webmvc.servlet.mvc.tobe;

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

        setUpRequest(request, "/get-test", "GET", "gugu");
        handleRequestAndAssert(request, response, "gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        setUpRequest(request, "/post-test", "POST", "gugu");
        handleRequestAndAssert(request, response, "gugu");
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void all(RequestMethod requestMethod) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        setUpRequest(request, "/all-test", requestMethod.name(), "gugu");
        handleRequestAndAssert(request, response, "gugu");
    }

    private void setUpRequest(HttpServletRequest request, String uri, String method, String id) {
        when(request.getAttribute("id")).thenReturn(id);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
    }

    private void handleRequestAndAssert(HttpServletRequest request, HttpServletResponse response, String expectedId) throws Exception {
        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);
        assertThat(modelAndView.getObject("id")).isEqualTo(expectedId);
    }
}
