package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    @Test
    void 중복된_요청_매핑_시_예외를_던진다() {
        var mapping = new AnnotationHandlerMapping("duplicatefixtures");

        assertThatThrownBy(mapping::initialize).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("creationFailureCases")
    void 컨트롤러_생성에_실패하면_예외를_던진다(String basePackage, Class<? extends Throwable> causeType) {

        var mapping = new AnnotationHandlerMapping(basePackage);

        assertThatThrownBy(mapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(causeType);
    }

    static Stream<Arguments> creationFailureCases() {
        return Stream.of(
                Arguments.of("creationFailureFixture.missingConstructor", NoSuchMethodException.class),
                Arguments.of("creationFailureFixture.inaccessible", IllegalAccessException.class),
                Arguments.of("creationFailureFixture.abstractType", InstantiationException.class),
                Arguments.of("creationFailureFixture.throwing", InvocationTargetException.class)
        );
    }
}
