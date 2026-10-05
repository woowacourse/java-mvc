package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import org.reflections.Reflections;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
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
    void supportsAllHttpMethodsWhenMethodIsNotSpecified(final RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/all-methods-test");
        when(request.getMethod()).thenReturn(method.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void prefersExplicitMappingAtSamePath() throws Exception {
        final var execution = handler("/priority-test", RequestMethod.GET);

        assertThat(execution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                .getObject("source")).isEqualTo("explicit");
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class, names = "GET", mode = EnumSource.Mode.EXCLUDE)
    void fallsBackToUnrestrictedMapping(final RequestMethod method) throws Exception {
        final var execution = handler("/priority-test", method);

        assertThat(execution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                .getObject("source")).isEqualTo("fallback");
    }

    @ParameterizedTest
    @ValueSource(strings = {"explicit", "any"})
    void rejectsDuplicateMappingDuringInitialization(final String fixture) {
        final var mapping = new AnnotationHandlerMapping("fixtures.duplicates." + fixture);

        assertThatThrownBy(mapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate handler mapping:")
                .hasMessageContaining("/duplicate");
    }

    @Test
    void failedReinitializationKeepsPreviouslyRegisteredHandlers() {
        final var previous = handler("/get-test", RequestMethod.GET);
        try (final var reflections = mockConstruction(Reflections.class, (mock, context) ->
                when(mock.getTypesAnnotatedWith(com.interface21.context.stereotype.Controller.class))
                        .thenReturn(Set.of(fixtures.duplicates.explicit.DuplicateController.class)))) {
            assertThatThrownBy(handlerMapping::initialize).isInstanceOf(IllegalStateException.class);
        }

        assertThat(handler("/get-test", RequestMethod.GET)).isSameAs(previous);
        assertThat(handler("/duplicate", RequestMethod.GET)).isNull();
    }

    @Test
    void returnsNoHandlerForUnmappedPath() {
        assertThat(handler("/missing", RequestMethod.GET)).isNull();
    }

    private HandlerExecution handler(final String path, final RequestMethod method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(path);
        when(request.getMethod()).thenReturn(method.name());
        return (HandlerExecution) handlerMapping.getHandler(request);
    }
}
