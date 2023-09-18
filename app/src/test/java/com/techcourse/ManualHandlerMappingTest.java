package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

class ManualHandlerMappingTest {

    private ManualHandlerMapping handlerMapping = new ManualHandlerMapping();

    @BeforeEach
    void setUp() {
        handlerMapping.initialize();
    }

    @DisplayName("HandlerMapping에 존재하는 Uri이면 해당 Controller를 반환한다.")
    @Test
    void getHandler() {
        // given
        final var request = mock(HttpServletRequest.class);

        // when
        when(request.getRequestURI()).thenReturn("/");
        Controller controller = handlerMapping.getHandler(request);

        // then
        assertThat(controller.getClass()).isEqualTo(ForwardController.class);
    }

    @DisplayName("HandlerMapping에 존재하지 않는 Uri이면 null을 반환한다.")
    @Test
    void getHandler_UriNotInMapping() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("?");

        // when & then
        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
