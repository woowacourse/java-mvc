package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void 요청을_처리할_수_있는_핸들러를_반환() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        HandlerMapping firstHandlerMapping = mock(HandlerMapping.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        when(firstHandlerMapping.getHandler(request)).thenReturn(handlerExecution);

        HandlerMapping secondHandlerMapping = mock(HandlerMapping.class);
        when(secondHandlerMapping.getHandler(request)).thenReturn(null);

        HandlerMappingRegistry handlerMappingRegistry = HandlerMappingRegistry.getInstance();
        handlerMappingRegistry.addHandlerMapping(firstHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(secondHandlerMapping);

        // when
        Object actual = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(actual).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void 요청을_처리할_수_있는_핸들러가_없다면_null을_반환() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        when(handlerMapping.getHandler(request)).thenReturn(null);

        HandlerMappingRegistry handlerMappingRegistry = HandlerMappingRegistry.getInstance();
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        // when
        Object actual = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(actual).isNull();
    }
}
