package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setup() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("핸들러를 처리한다.")
    @Test
    void handle() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        Object handler = handlerMapping.getHandler(request).orElseThrow();

        assertAll(
                () -> assertThat(handler).isInstanceOf(HandlerExecution.class),
                () -> assertThatCode(() -> handlerAdapter.handle(handler, request, response))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("핸들러를 처리할 수 있는지 판별한다.")
    @ParameterizedTest
    @MethodSource("randomHandlers")
    void canHandle(Object handler, boolean expected) {
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        boolean result = handlerAdapter.canHandle(handler);

        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> randomHandlers() {
        return Stream.of(
                Arguments.of(new HandlerExecution(AnnotationHandlerMappingTest.class.getEnclosingMethod()), true),
                Arguments.of(new ForwardController("/index.jsp"), false),
                Arguments.of("1234", false)
        );
    }
}
