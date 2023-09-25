package com.techcourse.controller;

import static com.techcourse.repository.InMemoryUserRepository.findByAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

class RegisterControllerTest {

    @DisplayName("새로운 사용자를 올바르게 저장할 수 있다.")
    @Test
    void save() {
        // given
        final RegisterController registerController = new RegisterController();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final User dino = new User(2L, "dino", "good", "bad@dino.com");

        when(request.getParameter("account")).thenReturn(dino.getAccount());
        when(request.getParameter("password")).thenReturn("good");
        when(request.getParameter("email")).thenReturn("bad@dino.com");

        // when
        registerController.save(request, response);

        // then
        Assertions.assertAll(
                () -> assertThat(findByAccount("dino")).isPresent(),
                () -> assertThat(findByAccount("dino").get()).usingRecursiveComparison().isEqualTo(dino)
        );
    }

    @DisplayName("회원가입 페이지를 반환한다.")
    @Test
    void show() {
        // given
        final RegisterController registerController = new RegisterController();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ModelAndView expected = new ModelAndView(new JspView("register.jsp"));

        // when
        final ModelAndView actual = registerController.show(request, response);

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
