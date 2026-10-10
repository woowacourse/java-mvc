package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMapping = manualHandlerMapping;
    }

    @Test
    void 요청_URL에_등록된_기존_컨트롤러를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        assertThat(handlerMapping.getHandler(request)).isInstanceOf(LoginController.class);
    }

    @Test
    void 등록되지_않은_URL이면_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unknown");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
