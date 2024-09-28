package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    public void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }
    
    @DisplayName("핸들러를 반환한다.")
    @Test
    void getHandler_returnHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        Object expectedHandler = mock(HandlerExecution.class);
        when(handlerMapping.getHandler(request)).thenReturn(expectedHandler);

        // when
        Object actual = handlerMappingRegistry.getHandler(request).get();

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("핸들러를 찾지 못하면 빈 옵셔널을 반환한다.")
    @Test
    void getHandler_returnEmptyOptional() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping handlerMapping = mock(HandlerMapping.class);

        Object expectedHandler = mock(HandlerExecution.class);
        when(handlerMapping.getHandler(request)).thenReturn(expectedHandler);

        // when
        Optional<Object> actual = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(actual).isEmpty();
    }
}
