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
    void 등록된_URL이면_컨트롤러를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        final var handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(LoginController.class);
    }

    @Test
    void HTTP_메서드와_무관하게_URL만으로_컨트롤러를_찾는다() {
        final var getRequest = mock(HttpServletRequest.class);
        when(getRequest.getRequestURI()).thenReturn("/login");
        when(getRequest.getMethod()).thenReturn("GET");
        final var postRequest = mock(HttpServletRequest.class);
        when(postRequest.getRequestURI()).thenReturn("/login");
        when(postRequest.getMethod()).thenReturn("POST");

        assertThat(handlerMapping.getHandler(getRequest)).isSameAs(handlerMapping.getHandler(postRequest));
    }

    @Test
    void 등록되지_않은_URL이면_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/not-registered");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
