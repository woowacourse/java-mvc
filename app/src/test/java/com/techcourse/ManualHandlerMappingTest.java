package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.controller.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

class ManualHandlerMappingTest {
    private ManualHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
    }

    @Test
    void root() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var view = (String) handlerExecution.handle(request, response);

        assertThat(view).isEqualTo("/index.jsp");
    }

    @Test
    void register_view() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/register/view");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var view = (String) handlerExecution.handle(request, response);

        assertThat(view).isEqualTo("/register.jsp");
    }
}
