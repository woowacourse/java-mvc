package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LogoutControllerTest {

    @DisplayName("/logout 경로로 GET 요청을 하면 session이 삭제되고 index.jsp 를 반환한다.")
    @Test
    void logout() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        LogoutController logoutController = new LogoutController();
        HttpSession session = mock(HttpSession.class);

        given(request.getSession()).willReturn(session);

        ModelAndView modelAndView = logoutController.logout(request, response);

        assertThat(request.getSession().getAttribute(UserSession.SESSION_KEY)).isNull();
        assertThat(modelAndView).isEqualTo(new ModelAndView(new JspView("redirect:/")));
    }
}
