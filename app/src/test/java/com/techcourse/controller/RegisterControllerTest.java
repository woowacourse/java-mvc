package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterControllerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    RegisterController registerController;

    @BeforeEach
    void setUp() {
        InMemoryUserRepository.removeAll();
        InMemoryUserRepository.save(new User(1L,"gugu", "password", "hkkang@woowahan.com"));

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        registerController = new RegisterController();
    }

    @DisplayName("회원가입에 성공하면 index.jsp로 리다이렉트 된다.")
    @Test
    void registerSuccess() {
        // given
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getParameter("account")).thenReturn("charlie");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getParameter("email")).thenReturn("test@test.com");

        // when
        ModelAndView modelAndView = registerController.register(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("redirect:/index.jsp");
    }

    @DisplayName("이미 존재하는 계정으로 회원가입하면 409.jsp로 리다이렉트 된다.")
    @Test
    void registerFailed() {
        // given
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getParameter("email")).thenReturn("test@test.com");

        // when
        ModelAndView modelAndView = registerController.register(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("409.jsp");
    }

    @Test
    void view() {
        // given
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());

        // when
        ModelAndView modelAndView = registerController.view(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("/register.jsp");
    }
}