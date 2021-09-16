package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestControllerTest {

    private RestController restController;

    @BeforeEach
    void setUp() {
        restController = new RestController();
    }

    @Test
    void index() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        ModelAndView modelAndView = restController.index(request, response);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }

    @Test
    void logout() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        ModelAndView modelAndView = restController.logout(request, response);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        assertThat(session.getAttribute(UserSession.SESSION_KEY)).isNull();
    }
}
