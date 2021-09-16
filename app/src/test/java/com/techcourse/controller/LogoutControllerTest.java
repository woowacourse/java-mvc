package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

public class LogoutControllerTest extends ControllerTest {
    @Test
    void post() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(httpSession);

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getView()).isEqualTo("redirect:/");
    }
}
