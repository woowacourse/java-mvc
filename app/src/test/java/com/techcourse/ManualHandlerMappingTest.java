package com.techcourse;

import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private ManualHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
    }

    @Test
    void 등록된_URI로_요청하면_컨트롤러를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        assertThat(handlerMapping.getHandler(request)).isInstanceOf(LoginController.class);
    }

    @Test
    void 등록되지_않은_URI로_요청하면_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/not-found");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
