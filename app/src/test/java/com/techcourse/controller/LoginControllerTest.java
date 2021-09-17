package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    LoginController loginController;

    @BeforeEach
    void setUp() {
        InMemoryUserRepository.removeAll();
        InMemoryUserRepository.save(new User(1L,"gugu", "password", "hkkang@woowahan.com"));

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        loginController = new LoginController();
    }

    @DisplayName("/login/view GET : 로그인 페이지 이동시 로그인하지 않은 상태면 login.jsp를 응답한다..")
    @Test
    void view() {
        // given
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);

        // when
        ModelAndView modelAndView = loginController.view(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("/login.jsp");
    }

    @DisplayName("/login/view GET : 로그인 페이지 이동시 이미 로그인 상태면 index.jsp로 리다이렉트한다.")
    @Test
    void viewAlreadyLoggedIn() {
        // given
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("user")).thenReturn(new User(1L, "gugu", "password", "hkkang@woowahan.com"));

        // when
        ModelAndView modelAndView = loginController.view(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("redirect:/index.jsp");
    }

    @DisplayName("/login POST : 이미 로그인된 상태일 시 index.jsp로 리다이렉트 된다.")
    @Test
    void executeAlreadyLoggedIn() {
        // given
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("user")).thenReturn(new User(1L, "gugu", "password", "hkkang@woowahan.com"));

        // when
        ModelAndView modelAndView = loginController.execute(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("redirect:/index.jsp");
    }

    @DisplayName("/login POST : 로그인에 성공하면 index.jsp로 리다이렉트 된다.")
    @Test
    void executeLoginSuccess() {
        // given
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        // when
        ModelAndView modelAndView = loginController.execute(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("redirect:/index.jsp");
    }

    @DisplayName("/login POST : 로그인에 실패하면 401.jsp로 리다이렉트 된다.")
    @Test
    void executeLoginFailed() {
        // given
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong password");
        when(request.getSession()).thenReturn(session);

        // when
        ModelAndView modelAndView = loginController.execute(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("redirect:/401.jsp");
    }

    @DisplayName("로그아웃을 성공하면 index.jsp로 리다이렉트된다.")
    @Test
    void logout() {
        // given
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getSession()).thenReturn(session);
        doNothing().when(session).removeAttribute(anyString());

        // when
        ModelAndView modelAndView = loginController.logout(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("redirect:/");
    }
}