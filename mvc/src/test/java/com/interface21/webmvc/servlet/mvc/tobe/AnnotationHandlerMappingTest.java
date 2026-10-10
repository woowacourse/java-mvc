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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
    @CsvSource({"GET, find-users", "POST, save-user"})
    void mapsSamePathByRequestMethod(final RequestMethod requestMethod, final String expectedRoute) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/users");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("route")).isEqualTo(expectedRoute);
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void mapsEveryRequestMethodWhenNoMethodIsSpecified(final RequestMethod requestMethod) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/all-methods");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("route")).isEqualTo("all-methods");
    }

    @Test
    void invokesPrivateMappedMethod() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/private-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("gugu");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        assertThat(handlerExecution).isNotNull();

        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("route")).isEqualTo("private-handler");
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @ParameterizedTest
    @CsvSource({"/unregistered, GET", "/get-test, POST"})
    void returnsNullWhenNoMappingMatches(final String path, final RequestMethod requestMethod) {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn(path);
        when(request.getMethod()).thenReturn(requestMethod.name());

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void sharesOneControllerInstanceAcrossMappedMethods() throws Exception {
        final var getRequest = mock(HttpServletRequest.class);
        final var postRequest = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(getRequest.getRequestURI()).thenReturn("/shared-controller");
        when(getRequest.getMethod()).thenReturn("GET");
        when(postRequest.getRequestURI()).thenReturn("/shared-controller");
        when(postRequest.getMethod()).thenReturn("POST");

        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);

        assertThat(getHandler.handle(getRequest, response).getObject("count")).isEqualTo(1);
        assertThat(postHandler.handle(postRequest, response).getObject("count")).isEqualTo(2);
    }
}
