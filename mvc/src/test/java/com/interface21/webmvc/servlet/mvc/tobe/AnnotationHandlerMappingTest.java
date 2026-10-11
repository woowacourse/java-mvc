package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
    void omittedMethodSupportsEveryHttpMethod(RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/all-methods");
        when(request.getMethod()).thenReturn(method.name());

        final var handler = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(handler.handle(request, response).getObject("method")).isEqualTo(method.name());
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class, names = {"GET", "POST"})
    void sameUrlSelectsHandlerByHttpMethod(RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/shared-url");
        when(request.getMethod()).thenReturn(method.name());

        final var handler = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(handler.handle(request, response).getObject("method")).isEqualTo(method.name());
    }

    @Test
    void duplicateMappingFailsOnInitialization() {
        final var duplicatedMapping = new AnnotationHandlerMapping("duplicatehandlers");

        assertThatThrownBy(duplicatedMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/duplicate")
                .hasMessageContaining("GET");
    }

    @Test
    void unregisteredUrlReturnsNoHandler() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/not-registered");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void postToGetOnlyUrlReturnsNoHandler() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
