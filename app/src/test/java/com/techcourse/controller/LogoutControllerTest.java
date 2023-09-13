package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogoutControllerTest {

    private LogoutController logoutController = new LogoutController();

    @Test
    void 로그아웃을_하면_인덱스를_반환한다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        doNothing().when(httpSession).removeAttribute(UserSession.SESSION_KEY);

        //when
        final var modelAndView = logoutController.execute(request, response);

        //then
        final var expected = new ModelAndView(new JspView("/"));
        Assertions.assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

}
