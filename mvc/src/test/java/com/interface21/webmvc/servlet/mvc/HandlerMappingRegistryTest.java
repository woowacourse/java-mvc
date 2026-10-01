package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry registry;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        registry = new HandlerMappingRegistry();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void 등록된_매핑이_처리할_수_있으면_그_핸들러를_반환한다() {
        final var handler = new Object();
        registry.addHandlerMapping(anyRequest -> handler);

        assertThat(registry.getHandler(request)).containsSame(handler);
    }

    @Test
    void 앞의_매핑이_처리할_수_없으면_다음_매핑의_핸들러를_반환한다() {
        final var handler = new Object();
        registry.addHandlerMapping(anyRequest -> null);
        registry.addHandlerMapping(anyRequest -> handler);

        assertThat(registry.getHandler(request)).containsSame(handler);
    }

    @Test
    void 여러_매핑이_처리할_수_있으면_먼저_등록된_매핑의_핸들러를_반환한다() {
        final var firstHandler = new Object();
        final var secondHandler = new Object();
        registry.addHandlerMapping(anyRequest -> firstHandler);
        registry.addHandlerMapping(anyRequest -> secondHandler);

        assertThat(registry.getHandler(request)).containsSame(firstHandler);
    }

    @Test
    void 모든_매핑이_처리할_수_없으면_빈_Optional을_반환한다() {
        registry.addHandlerMapping(anyRequest -> null);
        registry.addHandlerMapping(anyRequest -> null);

        assertThat(registry.getHandler(request)).isEmpty();
    }

    @Test
    void 등록된_매핑이_없으면_빈_Optional을_반환한다() {
        assertThat(registry.getHandler(request)).isEmpty();
    }
}
