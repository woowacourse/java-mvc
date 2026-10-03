package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void 첫_매핑에_핸들러가_없으면_다음에서_찾는다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping first = mock(HandlerMapping.class);
        HandlerMapping second = mock(HandlerMapping.class);
        Object handler = new Object();

        when(first.getHandler(request)).thenReturn(null);
        when(second.getHandler(request)).thenReturn(handler);

        HandlerMappingRegistry registry = HandlerMappingRegistry.empty();
        registry.addHandlerMapping(first);
        registry.addHandlerMapping(second);

        // when
        Optional<Object> actual = registry.getHandler(request);

        assertThat(actual).containsSame(handler);
    }

    @Test
    void 어떤_매핑에도_핸들러가_없으면_빈_결과를_반환한다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Object handler = new Object();

        HandlerMappingRegistry registry = HandlerMappingRegistry.empty();

        // when
        Optional<Object> actual = registry.getHandler(request);

        assertThat(actual).isEmpty();
    }

}