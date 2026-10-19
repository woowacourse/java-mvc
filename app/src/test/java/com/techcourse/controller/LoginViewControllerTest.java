package com.techcourse.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

class LoginViewControllerTest {

    @Test
    void userPresentSessionGoMain() throws Exception {
        //given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var session = mock(HttpSession.class);
        var user = new User(1L, "gugu", "1234", "gugu@gmail.com");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        //when
        var result = new LoginViewController().execute(request, response);

        result.getView().render(result.getModel(), request, response);

        //then
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void userHasNotSessionGoLoginPage() throws Exception {
        //given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var session = mock(HttpSession.class);
        var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(null);

        //when
        var result = new LoginViewController().execute(request, response);
        result.getView().render(result.getModel(), request, response);

        //then
        verify(dispatcher).forward(request, response);
    }
}
