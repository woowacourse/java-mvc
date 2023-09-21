package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class DispatcherServletTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @BeforeEach
    void setUp() {
        dispatcherServlet.init();
    }

    @DisplayName("/register GET (annotation)")
    @Test
    void registerGetTest() throws ServletException, IOException {
        // given
        given(request.getRequestURI()).willReturn("/register");
        given(request.getMethod()).willReturn("GET");
        given(request.getRequestDispatcher(any())).willReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    @DisplayName("/register POST (annotation)")
    void registerPostTest() throws ServletException {
        // given
        given(request.getRequestURI()).willReturn("/register");
        given(request.getMethod()).willReturn("POST");
        given(request.getParameter("account")).willReturn("account");
        given(request.getParameter("password")).willReturn("password");
        given(request.getParameter("email")).willReturn("email");
        given(request.getRequestDispatcher(any())).willReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        final User user = InMemoryUserRepository.findByAccount("account").get();
        assertThat(user.getAccount()).isEqualTo("account");
    }

    @Test
    @DisplayName("/login/view (manual)")
    void LoginViewPageTest() throws ServletException, IOException {
        // given
        final MockedStatic<UserSession> userSessionMockedStatic = mockStatic(UserSession.class);
        given(request.getRequestURI()).willReturn("/login/view");
        given(request.getMethod()).willReturn("GET");
        given(request.getRequestDispatcher(any())).willReturn(requestDispatcher);
        given(UserSession.isLoggedIn(any())).willReturn(false);


        // when
        dispatcherServlet.service(request, response);
        userSessionMockedStatic.close();

        // then
        verify(requestDispatcher, times(1)).forward(request, response);
    }
}
