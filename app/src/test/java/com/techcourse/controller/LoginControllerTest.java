package com.techcourse.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginControllerTest {

    private LoginController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        controller = new LoginController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
    }

    @Test
    void login() throws Exception {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getParameter("password")).thenReturn("password");

        final var modelAndView = controller.login(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void alreadyLoggedIn() throws Exception {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        final var modelAndView = controller.login(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(session, never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void wrongPassword() throws Exception {
        when(request.getParameter("password")).thenReturn("wrong-password");

        final var modelAndView = controller.login(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(session, never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void unknownUser() throws Exception {
        when(request.getParameter("account")).thenReturn("unknown-user");

        final var modelAndView = controller.login(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(session, never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/401.jsp");
    }
}
