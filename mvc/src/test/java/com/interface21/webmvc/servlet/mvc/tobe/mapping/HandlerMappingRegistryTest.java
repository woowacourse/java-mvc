package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerMapping handlerMapping;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMapping = mock(HandlerMapping.class);
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        request = mock(HttpServletRequest.class);
    }

    @DisplayName("handlerMapping 등록 시 initialize 메서드가 호출된다.")
    @Test
    void addHandlerMapping() {
        HandlerMapping handlerMapping = mock(HandlerMapping.class);

        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        verify(handlerMapping).initialize();
    }

    @DisplayName("요청에 맞는 handler를 반환한다.")
    @Test
    void getHandler() {
        Object expect = new Object();
        when(handlerMapping.getHandler(request)).thenReturn(expect);

        Optional<Object> actual = handlerMappingRegistry.getHandler(request);

        assertThat(actual).contains(expect);
    }

    @DisplayName("요청에 맞는 handler가 없으면 Optional.empty()를 반환한다.")
    @Test
    void getEmpty() {
        when(handlerMapping.getHandler(request)).thenReturn(null);

        Optional<Object> actual = handlerMappingRegistry.getHandler(request);

        assertThat(actual).isNotPresent();
    }
}
