package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    @Test
    @DisplayName("핸들러 매핑을 추가할 수 있다.")
    void append() {
        HandlerMappings handlerMappings = new HandlerMappings("samples");
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        Controller controller = mock(Controller.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/samples");
        when(request.getMethod())
                .thenReturn("GET");
        when(handlerMapping.getHandler(request))
                .thenReturn(controller);

        handlerMappings.appendHandlerMapping(handlerMapping);

        Object actual = handlerMappings.getHandler(request);
        assertThat(actual).isEqualTo(controller);
    }
}
