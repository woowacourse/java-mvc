package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @BeforeEach
    void setUp() {
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples.general"));
        handlerMappingRegistry.init();
    }

    @DisplayName("URI에 해당하는 어노테이션 기반 핸들러를 반환한다.")
    @Test
    void getHandlerWithAnnotation() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getRequestURI()).thenReturn("/get-test");

        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
