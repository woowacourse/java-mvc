package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingsTest {

    @DisplayName("알맞은 handler가 있는 경우 handler 반환")
    @Test
    void mappings_mapToHandler() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Object handler = new Object();
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(handlerMapping);

        //when
        when(handlerMapping.findHandler(request)).thenReturn(handler);

        //then
        assertThat(handlerMappings.mapToHandler(request)).isEqualTo(handler);
    }

    @DisplayName("알맞은 handler가 없는 경우 예외")
    @Test
    void mappings_notFound() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(handlerMapping);

        //when & then
        assertThatThrownBy(() -> handlerMappings.mapToHandler(request))
                .isInstanceOf(ServletException.class)
                .hasMessage("No handler found for requestURI: " + request.getRequestURI());
    }

}