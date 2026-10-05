package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void supportsAllMethodsWhenMethodIsNotSpecified(final RequestMethod requestMethod) throws Exception {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/all-methods");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var execution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(execution).isNotNull();
        assertThat(execution.handle(request, mock(HttpServletResponse.class)).getObject("handler"))
                .isEqualTo("all");
    }

    @ParameterizedTest
    @CsvSource({
            "/same-url, GET, get",
            "/same-url, POST, post",
            "/multiple-methods, GET, multiple",
            "/multiple-methods, POST, multiple"
    })
    void selectsHandlerByUrlAndMethod(final String url, final String method, final String expectedHandler) throws Exception {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);

        final var execution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(execution).isNotNull();
        assertThat(execution.handle(request, mock(HttpServletResponse.class)).getObject("handler"))
                .isEqualTo(expectedHandler);
    }

    @ParameterizedTest
    @CsvSource({
            "/missing, GET",
            "/get-test, POST",
            "/multiple-methods, DELETE",
            "/all-methods, UNKNOWN"
    })
    void returnsNullWhenNoHandlerMatches(final String url, final String method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void rejectsOverlappingMappings() {
        final var conflictingMapping = new AnnotationHandlerMapping("fixtures.mapping.conflict");

        assertThatThrownBy(conflictingMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/duplicate");
    }
}
