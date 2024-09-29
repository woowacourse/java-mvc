package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerMappingNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void 요청을_처리할_수_있는_핸들러를_반환() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("samples");

        // when
        Object actual = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(actual).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void 커스텀_HandlerMapping_추가() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        CustomHandler customHandler = new CustomHandler();
        HandlerMapping customHandlerMapping = mock(HandlerMapping.class);
        when(customHandlerMapping.getHandler(request)).thenReturn(customHandler);

        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("samples");

        // when
        handlerMappingRegistry.addHandlerMapping(customHandlerMapping);
        Object actual = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(actual).isInstanceOf(CustomHandler.class);
    }

    @Test
    void 요청을_처리할_수_있는_핸들러가_없다면_예외_발생() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("samples");

        // when, then
        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(HandlerMappingNotFoundException.class);
    }

    static class CustomHandler {
    }
}
