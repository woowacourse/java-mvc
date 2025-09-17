package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("getMethods vs getDefaultMethods")
    @Test
    void methods() {
        // given
        var annotationHandlerMappingClass = AnnotationHandlerMapping.class;
        var methods = annotationHandlerMappingClass.getMethods();
        var declaredMethods = annotationHandlerMappingClass.getDeclaredMethods();

        // when
        var methodNames = Arrays.stream(methods)
                .map(method -> method.getName())
                .toList();
        var declaredMethodNames = Arrays.stream(declaredMethods)
                .map(method -> method.getName())
                .toList();

        // then
        assertAll(
                () -> assertThat(methodNames)
                        .contains("initialize", "getHandler") // public method 포함
                        .doesNotContain("scanControllers", "scanRequestMappingMethods") // private method 불포함
                        .contains("toString", "equals"), // 상속 받은 메서드 포함
                () -> assertThat(declaredMethodNames)
                        .contains("initialize", "getHandler") // public method 포함
                        .contains("scanControllers", "scanRequestMappingMethods") // private method 포함
                        .doesNotContain("toString", "equals") // 상속 받은 메서드 불포함
        );
    }
}
