package com.interface21.webmvc.servlet.mvc.handler.mapping;

import com.interface21.webmvc.servlet.NoHandlerFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = HandlerMappingRegistry.empty();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.initialize();
    }

    @Test
    void 요청에_해당하는_핸들러를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMappingRegistry.getHandler(request)).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void 요청에_해당하는_핸들러가_없으면_NoHandlerFoundException이_발생한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/not-exist");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(NoHandlerFoundException.class)
                .hasMessageContaining("GET /not-exist");
    }
}
