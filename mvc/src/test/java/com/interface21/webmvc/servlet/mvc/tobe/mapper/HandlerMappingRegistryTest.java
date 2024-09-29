package com.interface21.webmvc.servlet.mvc.tobe.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import samples.TestManualHandlerMapping;

class HandlerMappingRegistryTest {

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @BeforeEach
    void setUp() {
        TestManualHandlerMapping manualHandlerMapping = new TestManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @ParameterizedTest
    @MethodSource("requestAndHandler")
    void 매핑된_컨트롤러가_있으면_Handler를_반환한다(String requestURI, String requestMethod, Class<?> expectedHandler) {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getMethod()).thenReturn(requestMethod);

        Object actual = handlerMappingRegistry.getHandler(request);

        assertThat(actual).isInstanceOf(expectedHandler);
    }

    public static Stream<Arguments> requestAndHandler() {
        return Stream.of(
                Arguments.arguments("/post-test", "POST", HandlerExecution.class),
                Arguments.arguments("/supported-uri", null, Controller.class)
        );
    }

    @Test
    void 매핑된_컨트롤러가_없으면_예외가_발생한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/unsupported-uri");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("지원하지 않는 Handler 입니다.");
    }
}
