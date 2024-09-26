package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void 요청을_처리할_수_있는_핸들러를_반환() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");

        HandlerMappingRegistry handlerMappingRegistry = HandlerMappingRegistry.getInstance();
        handlerMappingRegistry.initialize("samples");

        // when
        Object actual = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(actual).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void 요청을_처리할_수_있는_핸들러가_없다면_null을_반환() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unsupported");

        HandlerMappingRegistry handlerMappingRegistry = HandlerMappingRegistry.getInstance();
        handlerMappingRegistry.initialize("samples");

        // when
        Object actual = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(actual).isNull();
    }
}
