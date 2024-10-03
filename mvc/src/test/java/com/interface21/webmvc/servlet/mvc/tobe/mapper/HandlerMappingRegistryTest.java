package com.interface21.webmvc.servlet.mvc.tobe.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @BeforeEach
    void setUp() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @Test
    void 매핑된_컨트롤러가_있으면_Handler를_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        Object actual = handlerMappingRegistry.getHandler(request);

        assertThat(actual).isInstanceOf(HandlerExecution.class);
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
