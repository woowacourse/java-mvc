package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void 등록된_매핑에서_처음_발견한_핸들러를_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping first = mock(HandlerMapping.class);
        HandlerMapping second = mock(HandlerMapping.class);
        Object handler = new Object();
        when(first.getHandler(request)).thenReturn(null);
        when(second.getHandler(request)).thenReturn(handler);
        HandlerMappingRegistry registry = new HandlerMappingRegistry(List.of(first, second));

        assertThat(registry.getHandler(request)).isSameAs(handler);
    }

    @Test
    void 요청을_처리할_핸들러가_없으면_예외가_발생한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping mapping = mock(HandlerMapping.class);
        when(request.getRequestURI()).thenReturn("/unknown");
        HandlerMappingRegistry registry = new HandlerMappingRegistry(List.of(mapping));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> registry.getHandler(request))
                .withMessageContaining("/unknown");
    }
}
