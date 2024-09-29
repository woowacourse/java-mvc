package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        HandlerMapping handlerMapping = new HandlerMapping() {

            private final Map<String, Object> controllers = new HashMap<>();

            @Override
            public void initialize() {
                controllers.put("/test1", "test-controller1");
                controllers.put("/test2", "test-controller2");
            }

            @Override
            public Object getHandler(HttpServletRequest request) {
                String requestURI = request.getRequestURI();
                return controllers.get(requestURI);
            }
        };

        handlerMappingRegistry = new HandlerMappingRegistry();

        handlerMappingRegistry.addHandlerMapping(handlerMapping);
        handlerMappingRegistry.initialize();

        request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
    }

    @DisplayName("요청에 맞는 handler를 반환한다.")
    @Test
    void getHandler() {
        when(request.getRequestURI()).thenReturn("/test1");

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).contains("test-controller1");
    }

    @DisplayName("요청에 맞는 handler가 없으면 Optional.empty()를 반환한다.")
    @Test
    void getEmpty() {
        when(request.getRequestURI()).thenReturn("/test3");

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isEmpty();
    }
}
