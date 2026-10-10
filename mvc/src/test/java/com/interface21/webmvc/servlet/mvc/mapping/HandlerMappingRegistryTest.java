package com.interface21.webmvc.servlet.mvc.mapping;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {
    HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp(){
        handlerMappingRegistry = new HandlerMappingRegistry(List.of(
                mock(HandlerMapping.class)
        ));
    }

    @Test
    void 요청에_매핑된_핸들러가_없을_경우_예외를_던진다(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/none");

        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        when(handlerMapping.getHandler(request)).thenReturn(null);

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(NoSuchElementException.class);
    }
}
