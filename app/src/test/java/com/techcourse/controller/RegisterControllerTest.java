package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

class RegisterControllerTest {

    private final RegisterController registerController = new RegisterController();

    @Test
    @DisplayName("get요청이 올 시, /register.jsp를 반환한다.")
    void show() {
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        final ModelAndView actual = registerController.show(request, response);

        final ModelAndView expected = new ModelAndView(new JspView("/register.jsp"));
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("post 요청이 올 시, 회원가입 과정을 거치고, redirect:/index.jsp를 반환한다.")
    void save() {
        //given
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        final String account = "account";
        final String password = "password";
        final String email = "email";
        final long unvalidatedId = Long.MIN_VALUE;

        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn(email);

        //when
        final ModelAndView actual = registerController.save(request, response);

        //then
        final Optional<User> savedUser = InMemoryUserRepository.findByAccount(account);
        final Optional<User> expectedUser = Optional.of(
            new User(unvalidatedId, account, password, email));

        assertAll(
            () -> assertThat(actual.getView().getViewName())
                .isEqualTo("redirect:/index.jsp"),
            () -> assertThat(savedUser)
                .usingRecursiveComparison()
                .ignoringFields("value.id")
                .isEqualTo(expectedUser)
        );
    }
}
