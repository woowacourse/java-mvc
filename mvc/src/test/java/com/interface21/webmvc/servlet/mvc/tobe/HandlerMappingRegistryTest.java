package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("등록된 HandlerMapping에서 핸들러를 조회한다")
    void getHandler() {
        final var request = mock(HttpServletRequest.class);
        final var handler = new Object();
        final var handlerMapping = mock(HandlerMapping.class);
        when(handlerMapping.getHandler(request)).thenReturn(handler);

        final var registry = new HandlerMappingRegistry(new ArrayList<>());
        registry.addHandlerMapping(handlerMapping);

        assertThat(registry.getHandler(request)).contains(handler);
    }

    @Test
    @DisplayName("요청을 처리할 HandlerMapping이 없으면 빈 Optional을 반환한다")
    void getEmptyHandler() {
        final var request = mock(HttpServletRequest.class);
        final var handlerMapping = mock(HandlerMapping.class);
        final var registry = new HandlerMappingRegistry(
                new ArrayList<>(List.of(handlerMapping))
        );

        assertThat(registry.getHandler(request)).isEmpty();
    }
}
