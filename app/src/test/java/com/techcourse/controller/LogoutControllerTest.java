package com.techcourse.controller;

import com.techcourse.session.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("LogoutController 테스트")
class LogoutControllerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private LogoutController logoutController;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        logoutController = new LogoutController();
    }

    @DisplayName("LogoutController GET /logout 요청 테스트")
    @Test
    void getLogout() {
        // given
        when(request.getSession()).thenReturn(session);

        // when
        final ModelAndView modelAndView = logoutController.logout(request, response);
        final View view = modelAndView.getView();

        // then
        assertThat(view).isInstanceOf(JspView.class);
        verify(session, times(1)).removeAttribute(UserSession.SESSION_KEY);
    }
}