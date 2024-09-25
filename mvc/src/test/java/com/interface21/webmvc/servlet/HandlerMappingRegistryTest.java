package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.fake.HighPriorityHandlerMapping;
import com.interface21.webmvc.servlet.fake.LowPriorityHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(new LowPriorityHandlerMapping());
        registry.addHandlerMapping(new HighPriorityHandlerMapping());
        registry.initialize();
    }

    @DisplayName("요청과 매핑되는 핸들러를 반환한다.")
    @Test
    void successfulGetHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/jazz");
        when(request.getMethod()).thenReturn("GET");

        Optional<Object> handler = registry.getHandler(request);

        assertThat(handler).isPresent();
        assertThat(handler.get()).isEqualTo("jazz");
    }

    @DisplayName("요청과 매핑되는 핸들러를 찾지 못하면 Empty를 반환한다.")
    @Test
    void handlerNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/kargo");
        when(request.getMethod()).thenReturn("GET");

        Optional<Object> handler = registry.getHandler(request);

        assertThat(handler).isEmpty();
    }

    @DisplayName("핸들러 매핑 우선순위 순서대로 핸들러를 찾는다.")
    @Test
    void handlerMappingOrderTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/jazz");
        when(request.getMethod()).thenReturn("GET");

        Optional<Object> handler = registry.getHandler(request);

        assertThat(handler).isPresent();
        assertThat(handler.get()).isEqualTo(1130);
    }
}
