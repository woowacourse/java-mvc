package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.asis.ForwardController;
import org.junit.jupiter.api.Test;

public class ManualHandlerMappingTest {

    @Test
    void 매핑된_핸들러를_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(ForwardController.class);
    }
}
