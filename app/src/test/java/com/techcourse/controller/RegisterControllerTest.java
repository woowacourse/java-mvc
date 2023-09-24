package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static webmvc.org.springframework.web.servlet.view.JspView.REDIRECT_PREFIX;

class RegisterControllerTest {

    private RegisterController registerController = new RegisterController();

    @Test
    void 회원가입을_하면_인덱스를_반환한다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("email");

        //when
        final var modelAndView = registerController.execute(request, response);

        //then
        final var expected = new ModelAndView(new JspView(REDIRECT_PREFIX + "/index.jsp"));

        assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 회원가입을_하면_레포지토리에_저장된다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("email");

        //when
        registerController.execute(request, response);

        //then
        assertThat(InMemoryUserRepository.findByAccount("test").orElseThrow())
                .usingRecursiveComparison()
                .isEqualTo(new User(2, "test", "test", "email"));
    }

}
